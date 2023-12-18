package com.kane.practice.program.usefull;

import java.util.ArrayList;
import java.util.List;

public class OldNewListUpdate {
    public static List<Attachment> updateAttachmentList(List<Attachment> newDataList, List<Attachment> oldDataList) {
        // 遍历新数据列表
        for (Attachment newData : newDataList) {
            // 检查新数据是否已存在于旧数据列表中
            boolean exists = false;
            for (Attachment oldData : oldDataList) {
                if (oldData.id == newData.id) {
                    // 如果存在，更新旧数据
                    oldData.name = newData.name;
                    oldData.status = newData.status;
                    exists = true;
                    break;
                }
            }

            // 如果不存在，将新数据添加到旧数据列表中
            if (!exists) {
                oldDataList.add(newData);
            }
        }

        // 标记旧数据列表中的数据为失效状态
        for (Attachment oldData : oldDataList) {
            boolean exists = false;
            for (Attachment newData : newDataList) {
                if (oldData.id == newData.id) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                oldData.status = "失效";
            }
        }

        return oldDataList;
    }


    public static List<Attachment> updateAttachmentListGood(List<Attachment> newDataList, List<Attachment> oldDataList) {
        List<Attachment> result = new ArrayList<>();
        // 使用 Stream API 进行更简洁的实现
        result = oldDataList.stream()
                .peek(oldData -> {
                    // 查找匹配的新数据
                    Attachment newData = newDataList.stream()
                            .filter(data -> data.id == oldData.id)
                            .findFirst()
                            .orElse(null);

                    // 如果找到匹配的新数据，更新旧数据
                    if (newData != null) {
                        oldData.name = newData.name;
                        oldData.status = newData.status;
                    } else {
                        // 如果未找到匹配的新数据，标记为失效
                        oldData.status = "失效";
                    }
                })
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        // 将新数据列表中不存在的数据添加到旧数据列表中

        List<Attachment> finalResult = result;
        newDataList.stream()
                .filter(newData -> oldDataList.stream().noneMatch(oldData -> oldData.id == newData.id))
                .forEach(newData -> oldDataList.add(new Attachment(newData.id, newData.name, newData.status)));

        return finalResult;
    }

    public static List<Attachment> updateAttachmentListGood2(List<Attachment> newDataList, List<Attachment> oldDataList) {
        List<Attachment> result = new ArrayList<>();
        // 使用 Stream API 进行更简洁的实现
        oldDataList.stream()
                .forEach(oldData -> {
                    // 查找匹配的新数据
                    Attachment newData = newDataList.stream()
                            .filter(data -> data.id == oldData.id)
                            .findFirst()
                            .orElse(null);

                    // 如果找到匹配的新数据，更新旧数据
                    if (newData != null) {
                        oldData.name = newData.name;
                        oldData.status = newData.status;
                    } else {
                        // 如果未找到匹配的新数据，标记为失效
                        oldData.status = "失效";
                    }
                });

        // 将新数据列表中不存在的数据添加到旧数据列表中

        newDataList.stream()
                .filter(newData -> oldDataList.stream().noneMatch(oldData -> oldData.id == newData.id))
                .forEach(newData -> oldDataList.add(new Attachment(newData.id, newData.name, newData.status)));
//                .forEach(newData -> oldDataList.add(newData));
        return oldDataList;
    }

    public static void main(String[] args) {
        // 示例数据
        List<Attachment> newDataList = new ArrayList<>();
        newDataList.add(new Attachment(1, "新附件1", "有效"));
        newDataList.add(new Attachment(2, "新附件2", "有效"));
        newDataList.add(new Attachment(3, "新附件3", "有效"));

        List<Attachment> oldDataList = new ArrayList<>();
        oldDataList.add(new Attachment(1, "旧附件1", "有效"));
        oldDataList.add(new Attachment(2, "旧附件2", "有效"));
        oldDataList.add(new Attachment(4, "旧附件4", "有效"));

        // 调用更新函数
//        List<Attachment> updatedDataList = updateAttachmentList(newDataList, oldDataList);

        List<Attachment> updatedDataList = updateAttachmentListGood2(newDataList, oldDataList);


        // 打印结果
        for (Attachment attachment : updatedDataList) {
            System.out.println("ID: " + attachment.id + ", 名称: " + attachment.name + ", 状态: " + attachment.status);
        }
    }


    static class Attachment {
        int id;
        String name;
        String status;

        Attachment(int id, String name, String status) {
            this.id = id;
            this.name = name;
            this.status = status;
        }
    }

}
