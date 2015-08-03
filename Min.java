package com.TerominoProject;

public class Min {
    public Integer minInReverseList(int array[]) throws Exception {
        if (array == null || array.length <= 0)
            throw new Exception("去你妈的");

        int leftIndex = 0;
        int rightIndex = array.length - 1;
        int midIndex = leftIndex;

        while (array[leftIndex] >= array[rightIndex]) {
            if (leftIndex - rightIndex == 1) {
                midIndex = rightIndex;
                break;
            }
            midIndex = (leftIndex + rightIndex) / 2;
            if (array[leftIndex] == array[rightIndex] && array[midIndex] == array[rightIndex]) {
                return MinInOrder(array, leftIndex, rightIndex);
            }

            if (array[midIndex] >= array[leftIndex]) {
                leftIndex = midIndex;
            } else if (array[midIndex] <= array[rightIndex]) {
                rightIndex = midIndex;
            }
        }
        return array[midIndex];
    }

    public Integer MinInOrder(int array[], int leftIndex, int rightIndex) {
        int result = array[leftIndex];

        for (int i = leftIndex + 1; i < rightIndex; i++) {
            if (result > array[i])
                result = array[i];
        }
        return result;
    }

}

