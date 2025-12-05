package com.yupi.yuoj.manager;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // 校验命令行参数是否合法
        if (args.length < 2) {
            System.err.println("用法错误！正确格式：java Main 数组元素1,元素2,元素3... 目标值");
            System.err.println("示例：java Main 2,7,11,15 9");
            return;
        }

        try {
            // 1. 解析命令行参数：第一个参数是数组（逗号分隔），第二个是目标值
            String[] numStrs = args[0].split(","); // 分割数组字符串（如 "2,7,11,15" → ["2","7","11","15"]）
            int[] nums = new int[numStrs.length];
            for (int i = 0; i < numStrs.length; i++) {
                nums[i] = Integer.parseInt(numStrs[i]); // 转换为整数数组
            }
            int target = Integer.parseInt(args[1]); // 转换目标值

            // 2. 调用两数之和方法
            int[] result = twoSum(nums, target);

            // 3. 输出结果
            System.out.printf("%d,%d",result[0], result[1]);

        } catch (NumberFormatException e) {
            System.err.println("参数格式错误！数组元素和目标值必须是整数");
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * 哈希表优化解法：O(n) 时间复杂度，支持命令行传参场景
     */
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            map.put(nums[i], i);
        }
        throw new IllegalArgumentException("无满足条件的两个整数");
    }
}