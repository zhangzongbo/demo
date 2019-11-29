package com.example.demo.leetcode;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangzongbo
 * @date 19-11-27 下午5:29
 */

@Slf4j
public class AddTowNumbers {


    private static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            this.val = x;
        }
    }

    /**
     * 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
     * 输出：7 -> 0 -> 8
     * 原因：342 + 465 = 807
     */
    private static ListNode add(ListNode l1, ListNode l2) {

        ListNode dummyHead = new ListNode(0);
        ListNode p = l1, q = l2, curr = dummyHead;
        int carry = 0;

        while (p != null || q != null) {

            int x = p != null ? p.val : 0;
            int y = q != null ? q.val : 0;
            int sum = carry + x + y;
            carry = sum / 10;
            curr.next = new ListNode(sum % 10);
            curr = curr.next;
            if (p != null) {
                p = p.next;
            }
            if (q != null) {
                q = q.next;
            }
        }

        if (carry > 0) {
            curr.next = new ListNode(carry);
        }

        return dummyHead.next;

    }

    private static ListNode insert(Integer[] s){
        ListNode h = new ListNode(s[0]);
        ListNode l = new ListNode(s[0]);
        for (int i = 1; i < s.length; i++){
            if (i == 1){
                l = new ListNode(s[i]);
                h.next = l;
            }else {
                l.next = new ListNode(s[i]);
                l = l.next;
            }
        }
        return h;
    }

    public static void main(String[] args) {

        Integer[] nums1 = {2, 4, 3};
        ListNode l1 = insert(nums1);

        Integer[] nums2 = {5, 6, 4};
        ListNode l2 = insert(nums2);



        ListNode result = add(l1, l2);
        while (result != null){
            log.info("{}", result.val);
            result = result.next;
        }
    }
}
