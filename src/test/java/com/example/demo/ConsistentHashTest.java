package com.example.demo;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * TreeMap 实现 一致性hash
 *
 * @author zhangzongbo
 * @date 19-11-21 下午6:46
 */
@Slf4j
public class ConsistentHashTest {

    static class Obj{
        String key;
        Obj(String key){
            this.key = key;
        }

        @Override
        public int hashCode(){
            return key.hashCode();
        }

        @Override
        public String toString(){

            return "Obj{" + "key='" + key + "'" + "}";
        }
    }

    static class Node{
        Map<Integer, Obj> node = new HashMap<>();
        String name;

        Node(String name){
            this.name = name;
        }

        public void pubObj(Obj obj){
            node.put(obj.hashCode(), obj);
        }

        Obj getObj(Obj obj){
            return node.get(obj.hashCode());
        }

        @Override
        public int hashCode(){
            return name.hashCode();
        }
    }

    /*
    非一致性hash
    集群是一个数组(存放节点hashMap), 添加节点,数组长度加一,
    添加元素时, key.hashcode % size 计算数组下标,决定放进哪个节点
    取元素时, key.hashcode % size 决定去哪个节点取数据
    添加节点后,size 变化, 继续用同样算法会找不到对应节点,故而取不到对应元素
      */
    static class NodeArray{
        Node[] nodes = new Node[1024];
        int size = 0;

        public void addNode(Node node){
            nodes[size++] = node;
        }

        Obj get(Obj obj){

            /* 添加新节点后,size 变了, get时没法找到*/
            int index = obj.hashCode() % size;
            return nodes[index].getObj(obj);
        }

        void put(Obj obj){

            /* put时根据size取模,寻找数组下标 */
            int index = obj.hashCode() % size;
            nodes[index].pubObj(obj);
        }
    }

    /*
    一致性hash
    集群是一个TreeMap<Integer, Node>,存放节点Node(hashMap), 节点中存放元素(普通obj)
    treeMap的keys是Node的hashcode, 因此集群中存放的 节点Node是有序的, 按照Node的hashcode 排序
    添加元素时, 首先计算元素的hashcode, 用此hashcode尝试从treeMap 中获取node, 获取到后直接往Node(hashMap) 中添加,
    没有获取到时则根据元素的hashcode 将treeMap 分为两部分, 找后一部分中最近的一个, 即没有直接命中的node时, 往大找最接近的一个Node 插入元素
    get时同理
    由于这样操作时, 存取元素与Node个数没有关系, 增减Node不会导致元素找不到元素
    利用treeMap 有序的特性, 能够快速(O(logN))找到稍大的最接近的Node,实现了一致性hash
      */
    static class NodeArrayTreeMap{

        /* TreeMap 会根据键排序*/
        TreeMap<Integer, Node> nodes = new TreeMap<>();

        void addNode(Node node){
            nodes.put(node.hashCode(), node);
        }

        void put(Obj obj){
            int objHashcode = obj.hashCode();
            Node node = nodes.get(objHashcode);
            if (node != null){
                node.pubObj(obj);
                return;
            }

            SortedMap<Integer, Node> tailMap = nodes.tailMap(objHashcode);
            int nodeHashcode = tailMap.isEmpty() ? nodes.firstKey() : tailMap.firstKey();
            nodes.get(nodeHashcode).pubObj(obj);
        }

        Obj get(Obj obj){
            Node node = nodes.get(obj.hashCode());
            if (node != null){
                return node.getObj(obj);
            }

            SortedMap<Integer, Node> tailMap = nodes.tailMap(obj.hashCode());
            int nodeHashcode = tailMap.isEmpty() ? nodes.firstKey() : tailMap.firstKey();
            return nodes.get(nodeHashcode).getObj(obj);
        }


    }

    /**
     * 验证普通 hash 对于增减节点，原有会不会出现移动。
     */
    public static void main(String[] args) {

        NodeArrayTreeMap nodeArray = new NodeArrayTreeMap();

        Node[] nodes = {
                new Node("Node--> 1"),
                new Node("Node--> 2"),
                new Node("Node--> 3")
        };

        for (Node node : nodes) {
            nodeArray.addNode(node);
        }

        Obj[] objs = {
                new Obj("1"),
                new Obj("2"),
                new Obj("3"),
                new Obj("4"),
                new Obj("5")
        };

        for (Obj obj : objs) {
            nodeArray.put(obj);
        }

        validate(nodeArray, objs);
    }
    private static void validate(NodeArrayTreeMap nodeArray, Obj[] objs) {
        for (Obj obj : objs) {
            System.out.println(nodeArray.get(obj));
        }

        nodeArray.addNode(new Node("anything1"));
        nodeArray.addNode(new Node("anything2"));

        System.out.println("========== after  =============");

        for (Obj obj : objs) {
            System.out.println(nodeArray.get(obj));
        }
    }

}
