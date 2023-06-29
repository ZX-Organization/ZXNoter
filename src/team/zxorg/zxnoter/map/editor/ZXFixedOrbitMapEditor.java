package team.zxorg.zxnoter.map.editor;

import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.map.mapInfo.ZXMInfo;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;
import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;
import team.zxorg.zxnoter.note.fixedorbit.LongNote;
import team.zxorg.zxnoter.note.fixedorbit.SlideNote;

import java.util.ArrayList;
import java.util.LinkedList;

public class ZXFixedOrbitMapEditor {
    /**
     * 实际zxMap
     */
    public ZXMap srcMap;
    /**
     * 虚影map
     */
    public ZXMap shadowMap;
    /**
     * 操作栈
     */
    LinkedList<MapOperate> operateStack;
    /**
     * 撤回栈
     */
    LinkedList<MapOperate> withdrawStack;

    public static int withdrawStackSize;
    /**
     * 虚影缓存
     */
    public static final int LONG_NOTE = 1;
    public static final int SLIDE_NOTE = 2;
    /**
     * 自动时间修正3ms
     */
    public static int AUTO_FIX_MISTAKE = 3;
    private ArrayList<FixedOrbitNote> shadows;
    /**
     * 操作缓存
     */
    private MapOperate tempMapOperate;

    /**
     *
     */
    private ArrayList<FixedOrbitNote> tempAddList;

    public ZXFixedOrbitMapEditor(ZXMap map) {
        srcMap = map;
        this.shadowMap = new ZXMap(new ArrayList<>(), map.timingPoints, map.unLocalizedMapInfo);
        operateStack = new LinkedList<>();
        withdrawStack = new LinkedList<>();
        shadows = new ArrayList<>();
        tempAddList = new ArrayList<>();
        withdrawStackSize=32;
    }

    /**
     * 编辑按键的时间与轨道变化(可直接编辑shadowMap中的按键)
     *
     * @param note       要编辑的按键
     * @param orbit      轨道
     * @param time       时间
     * @param isAbsolute 是否使用绝对
     * @return 移动后按键所处的下标位置
     */
    public int move(FixedOrbitNote note, int orbit, long time, boolean isAbsolute) {

        FixedOrbitNote shadowNote = checkOperate(note);

        //对虚影按键进行编辑
        if (isAbsolute)
            shadowMap.moveNote(shadowNote, orbit);
        else
            shadowMap.moveNote(shadowNote, shadowNote.orbit + orbit);

        //编辑时间戳
        int res;
        //对虚影按键进行编辑
        if (isAbsolute)
            res = shadowMap.moveNote(shadowNote, time);
        else
            res = shadowMap.moveNote(shadowNote, shadowNote.timeStamp + time);

        return res;

    }

    /**
     * 编辑组合键中某一子键的时间与轨道变化(此子键必须是长条)
     *
     * @param note          要编辑的子键的所属组合键
     * @param orbit         轨道
     * @param childIndex    子键下标
     * @param isAbsolute    是否使用绝对轨道
     * @param keepAfterNote 是否保持此子键之后所有按键的绝对位置
     */
    public boolean move(ComplexNote note, int orbit, int childIndex, boolean keepAfterNote, boolean isAbsolute) {
        ComplexNote shadowNote = (ComplexNote) checkOperate(note);
        //编辑子键
        FixedOrbitNote child = shadowNote.notes.get(childIndex);
        int orbitChanges = orbit;
        if (child instanceof LongNote childLongNote) {
            //直接编辑子键
            if (isAbsolute) {
                orbitChanges = orbit - childLongNote.orbit;
                childLongNote.orbit = orbit;
            } else {
                childLongNote.orbit += orbit;
            }
            //区分是否保持后方子键位置与时间戳
            if (keepAfterNote) {
                //保持(后方不需要移动)
                //检查此子键下一个按键
                if (childIndex == shadowNote.notes.size() - 1) {
                    //子键处于组合键尾部无需操作
                } else {
                    FixedOrbitNote next = shadowNote.notes.get(childIndex + 1);
                    if (next instanceof SlideNote slideNote) {
                        if (childLongNote.orbit != slideNote.orbit) {
                            //编辑的按键和下一个按键轨道错位
                            //缓存下下个物件的轨道位置
                            int nextNextOrbit = slideNote.orbit + slideNote.slideArg;
                            slideNote.orbit = childLongNote.orbit;
                            //连接断开的滑键
                            slideNote.slideArg = nextNextOrbit - slideNote.orbit;
                            if (shadowNote.notes.size() - 1 == childIndex + 1) {
                                //下一个按键即为尾按键
                            }
                        }
                    }
                }
            } else {
                //后段子键跟随编辑
                if (childIndex == shadowNote.notes.size() - 1) {
                    //判断编辑的子键是否为尾子键
                }
                //跟随
                for (int i = childIndex + 1; i < shadowNote.notes.size(); i++) {
                    shadowNote.notes.get(i).orbit += orbitChanges;
                }
            }
            //检查组合键是否有断裂(从此子键上一个子键检查到此子键的下一个子键)
            //最后检查,防止打乱下标
            //检查此子键前一个按键
            if (childIndex == 0) {
                //子键处于组合键头部(头部按键只能编辑长键[拉出])
                //头部添加一个滑键
                shadowNote.notes.add(new SlideNote(shadowNote.timeStamp, shadowNote.orbit, childLongNote.orbit - shadowNote.orbit));
                shadowNote.notes.sort(FixedOrbitNote::compareTo);
            } else {
                FixedOrbitNote previous = shadowNote.notes.get(childIndex - 1);
                if (previous instanceof SlideNote slideNote) {
                    //编辑的(child)是长条,前一个(previous)一定是滑键
                    if (slideNote.orbit + slideNote.slideArg != child.orbit) {
                        //修正前一个滑键参数
                        slideNote.slideArg = child.orbit - slideNote.orbit;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 编辑组合键中某一子键的时间戳变化(此子键必须是滑键)
     *
     * @param note          要编辑的子键的所属组合键
     * @param time          要编辑的子键的时间戳变化
     * @param childIndex    要编辑的子键索引
     * @param keepAfterNote 是否保持此子键之后所有按键的绝对位置
     * @param isAbsolute    是否使用绝对时间戳
     * @return 编辑是否成功
     */
    public boolean move(ComplexNote note, long time, int childIndex, boolean keepAfterNote, boolean isAbsolute) {
        //操作时间戳并排序
        ComplexNote shadowNote = (ComplexNote) checkOperate(note);
        //编辑子键
        FixedOrbitNote child = shadowNote.notes.get(childIndex);

        long timeChange = time;
        //此子键必定为滑键
        if (child instanceof SlideNote childSlideNote) {
            //直接修改
            if (isAbsolute) {
                timeChange = time - childSlideNote.timeStamp;
                childSlideNote.timeStamp = time;
            } else {
                childSlideNote.timeStamp += time;
            }
            if (keepAfterNote) {
                if (childIndex == shadowNote.notes.size() - 1) {
                    //此滑键处于组合键尾部
                } else {
                    //修改后一子键参数
                    FixedOrbitNote nextNote = shadowNote.notes.get(childIndex + 1);
                    if (nextNote instanceof LongNote nextLongNote) {
                        if (nextLongNote.timeStamp != childSlideNote.timeStamp) {
                            nextLongNote.timeStamp = childSlideNote.timeStamp;
                            //同步缩减持续时间
                            nextLongNote.sustainedTime -= timeChange;
                        }
                    }
                }
            } else {
                //跟随编辑
                for (int index = childIndex + 1; index < shadowNote.notes.size(); index++) {
                    shadowNote.notes.get(index).timeStamp += timeChange;
                }
            }
            //检查组合键是否有断裂(从此子键上一个子键检查到此子键的下一个子键)
            //最后检查,防止打乱下标
            //检查此子键前一个按键
            if (childIndex == 0) {
                //子键处于组合键头部(头部按键只能编辑长键[拉出])
                //头部添加一个长键
                shadowNote.notes.add(new LongNote(shadowNote.timeStamp, shadowNote.orbit, childSlideNote.timeStamp - shadowNote.timeStamp));
                shadowNote.notes.sort(FixedOrbitNote::compareTo);
            } else {
                FixedOrbitNote previous = shadowNote.notes.get(childIndex - 1);
                if (previous instanceof LongNote previousLongNote) {
                    //编辑的(child)是滑键,前一个(previous)一定是长键
                    if (previousLongNote.timeStamp + previousLongNote.sustainedTime != childSlideNote.timeStamp) {
                        //修正前一个长键参数
                        previousLongNote.sustainedTime = childSlideNote.timeStamp - previousLongNote.timeStamp;
                    }
                }
            }
        }
        return true;
    }


    /**
     * 向按键键尾部添加新的子键
     *
     * @param srcNote   原按键
     * @param parameter 要添加的按键参数
     * @param type      要添加的按键类型
     * @return 添加是否成功
     */
    public ComplexNote addEndOfNote(FixedOrbitNote srcNote, long parameter, int type) {
        FixedOrbitNote shadowNote = checkOperate(srcNote);
        ComplexNote tempComplexNote;
        //传入的已经属于组合键
        if (shadowNote instanceof ComplexNote srcComplexNote) {
            //组合键
            tempComplexNote = srcComplexNote;
        } else if (shadowNote instanceof LongNote srcLongNote) {
            //长键
            tempComplexNote = new ComplexNote(srcLongNote.timeStamp, srcLongNote.orbit);
            tempComplexNote.addNote(srcLongNote.clone());
        } else if (shadowNote instanceof SlideNote srcSlideNote) {
            //滑键
            tempComplexNote = new ComplexNote(srcSlideNote.timeStamp, srcSlideNote.orbit);
        } else {
            //单键
            tempComplexNote = new ComplexNote(srcNote.timeStamp, srcNote.orbit);
        }
        //生成子键
        FixedOrbitNote child;
        if (type == LONG_NOTE) {
            child = new LongNote(tempComplexNote.timeStamp, tempComplexNote.orbit, parameter);
        } else {
            child = new SlideNote(tempComplexNote.timeStamp, tempComplexNote.orbit, (int) parameter);
        }

        //尝试获取此组合键尾部子键
        FixedOrbitNote endNote = null;
        if (tempComplexNote.notes.size() > 0) {
            endNote = tempComplexNote.notes.get(tempComplexNote.notes.size() - 1);
        }

        if (endNote != null) {
            //获取到有其他按键,更新为结尾按键所处轨道与时间戳
            if (type == LONG_NOTE) {
                child = new LongNote(endNote.timeStamp, endNote.orbit, parameter);
            } else {
                child = new SlideNote(endNote.timeStamp, endNote.orbit, (int) parameter);
            }
        }

        //获得添加列表尾部按键
        FixedOrbitNote addLastNote = null;
        if (tempAddList.size() > 0) {
            addLastNote = tempAddList.get(tempAddList.size() - 1);
        }

        //合并尾部同类型按键
        if (addLastNote != null) {
            //默认使用上一按键的轨道和时间戳
            child.orbit = addLastNote.orbit;
            child.timeStamp = addLastNote.timeStamp;
            //首先判断要新添加的按键
            if (child instanceof SlideNote newSlideNote) {
                //若上一个添加的也是滑键,直接修改上一个滑键滑动参数为新按键的参数
                if (addLastNote instanceof SlideNote lastSlideNote)
                    lastSlideNote.slideArg = newSlideNote.slideArg;
                //若上一个添加的是长条,此次添加新滑键
                if (addLastNote instanceof LongNote lastLongNote) {
                    //更新时间戳
                    newSlideNote.timeStamp = lastLongNote.timeStamp + lastLongNote.sustainedTime;
                    tempAddList.add(newSlideNote);
                }

            }
            if (child instanceof LongNote newLongNote) {
                //若上一个添加的也是长键,直接修改上一个长键持续时间为新按键的持续时间
                if (addLastNote instanceof LongNote lastLongNote)
                    lastLongNote.sustainedTime = newLongNote.sustainedTime;
                //若上一个添加的是滑键,此次添加新长键
                if (addLastNote instanceof SlideNote lastSlideNote) {
                    //同时变更轨道
                    newLongNote.orbit = lastSlideNote.orbit + lastSlideNote.slideArg;
                    tempAddList.add(newLongNote);
                }
            }
        } else {
            //上次未添加任何按键,保证按键类型不同后添加
            if (endNote != null) {
                if ((endNote instanceof LongNote endLongNote && child instanceof SlideNote)) {
                    child.timeStamp = endLongNote.timeStamp + endLongNote.sustainedTime;
                    tempAddList.add(child);
                }
                if (endNote instanceof SlideNote endSlideNote && child instanceof LongNote) {
                    //上一个是滑键,需要更新轨道
                    child.orbit = endSlideNote.orbit + endSlideNote.slideArg;
                    tempAddList.add(child);
                }
            } else {
                //不存在尾按键(由单键空包而来)
                tempAddList.add(child);
            }

        }

        //最后将添加列表加入组合键
        tempComplexNote.notes.addAll(tempAddList);
        checkComplexNote(tempComplexNote, false);
        if (!(shadowNote instanceof ComplexNote)) {
            //替换虚影为处理后的按键
            shadowMap.notes.removeAll(shadows);
            shadowMap.deleteNote(shadowNote);
            shadows.remove(shadowNote);
            shadows.clear();
            shadowMap.insertNote(tempComplexNote);
            shadows.add(tempComplexNote);
        }
        return tempComplexNote;
    }


    /**
     * 编辑按键参数(变化,非直接修改)
     *
     * @param parameter 参数变化值
     * @return 编辑是否成功
     */
    public boolean modifyPar(FixedOrbitNote note, long parameter, boolean isAbsolute) {
        FixedOrbitNote shadowNote = checkOperate(note);
        if (shadowNote instanceof SlideNote slideNote) {
            if (isAbsolute) {
                slideNote.slideArg = (int) parameter;
            } else {
                slideNote.slideArg += (int) parameter;
            }
            return true;
        } else if (shadowNote instanceof LongNote longNote) {
            if (isAbsolute) {
                longNote.sustainedTime = parameter;
            } else {
                longNote.sustainedTime += parameter;
            }
            return true;
        }
        return false;
    }

    /**
     * 编辑组合键中尾部子键的参数(编辑,非直接修改)
     *
     * @param complexNote 要编辑的组合键
     * @param parameter   要编辑的参数
     * @param isAbsolute  是否使用绝对
     * @return
     */
    public boolean modifyEndPar(ComplexNote complexNote, long parameter, boolean isAbsolute) {
        ComplexNote shadowNote = (ComplexNote) checkOperate(complexNote);
        FixedOrbitNote endChildNote = shadowNote.notes.get(shadowNote.notes.size() - 1);
        if (endChildNote instanceof SlideNote slideNote) {
            if (isAbsolute) {
                slideNote.slideArg = (int) parameter;
            } else {
                slideNote.slideArg += (int) parameter;
            }
            checkComplexNote(complexNote, true);
            return true;
        } else if (endChildNote instanceof LongNote longNote) {
            if (isAbsolute) {
                longNote.sustainedTime = parameter;
            } else {
                longNote.sustainedTime += parameter;
            }
            checkComplexNote(complexNote, true);
            return true;
        }
        return false;
    }

    /**
     * 删除组合键指定子键
     * @param complexNote 组合键
     * @param index 子键下标
     */
    public void deleteChildNote(ComplexNote complexNote , int index){
        ComplexNote shadowNote = (ComplexNote) checkOperate(complexNote);
        if (index+1 == complexNote.notes.size()){
            shadowNote.notes.remove(index);
            return;
        }
        FixedOrbitNote next = shadowNote.notes.get(index + 1);
        ComplexNote childCom1 = new ComplexNote(shadowNote.timeStamp , shadowNote.orbit);
        ComplexNote childCom2 = new ComplexNote(next.timeStamp , next.orbit);
        for (int i = 0; i < index; i++) {
            childCom1.addNote(shadowNote.notes.get(i).clone());
        }
        for (int i = index+1; i < shadowNote.notes.size(); i++) {
            childCom2.addNote(shadowNote.notes.get(i).clone());
        }
        shadows.add(childCom1);
        shadows.add(childCom2);
        shadowMap.notes.add(childCom1);
        shadowMap.notes.add(childCom2);
        shadowMap.notes.remove(shadowNote);
    }
    /**
     * 完成修改,同步原map,操作栈
     */
    public void modifyDone() {
        //完成修改,同步到原zxMap中
        if (tempMapOperate == null) {
            return;
        }
        tempMapOperate.desNotes.addAll(shadowMap.notes);
        //检查操作结果中是否包含组合键
        for (BaseNote tempEditNote : tempMapOperate.desNotes) {
            //删除按键操作
            if (tempEditNote == null) {
                continue;
            }
            if (tempEditNote instanceof ComplexNote complexNote) {
                //排序
                complexNote.notes.sort(FixedOrbitNote::compareTo);

                //第一遍基本修正0参和非法长条
                checkComplexNote(complexNote, false);
                //第二遍基本修正重合滑键和同时间滑键
                checkComplexNote(complexNote, false);
                //第三遍修正由于删除同时滑键出现的同轨长条,最后合并
                checkComplexNote(complexNote, true);

                complexNote.notes.sort(FixedOrbitNote::compareTo);
            } else if (tempEditNote instanceof LongNote longNote) {
                //检查长键参数
                if (longNote.sustainedTime == 0) {
                    shadowMap.notes.remove(longNote);
                    shadows.remove(longNote);
                    FixedOrbitNote fixedOrbitNote = new FixedOrbitNote(longNote.timeStamp, longNote.orbit);
                    shadowMap.insertNote(fixedOrbitNote);
                    shadows.add(fixedOrbitNote);
                }
            } else if (tempEditNote instanceof SlideNote slideNote) {
                //检查滑键参数
                if (slideNote.slideArg == 0) {
                    shadowMap.notes.remove(slideNote);
                    shadows.remove(slideNote);
                    FixedOrbitNote fixedOrbitNote = new FixedOrbitNote(slideNote.timeStamp, slideNote.orbit);
                    shadowMap.insertNote(fixedOrbitNote);
                    shadows.add(fixedOrbitNote);
                }
            }
        }
        tempMapOperate.desNotes.clear();
        tempMapOperate.desNotes.addAll(shadowMap.notes);
        //删除原按键
        for (BaseNote note : tempMapOperate.srcNotes)
            if (note != null) {
                //非新建
                srcMap.unLocalizedMapInfo.addInfo(
                        ZXMInfo.ObjectCount,
                        String.valueOf(
                                Integer.parseInt(srcMap.unLocalizedMapInfo.getInfo(ZXMInfo.ObjectCount)) - srcMap.deleteNote(note)
                        )
                );
            }
        int count = 0;
        //克隆结果插入原map
        for (BaseNote note : tempMapOperate.desNotes) {

            count+= srcMap.insertNote(note.clone());
        }
        //修改信息
        srcMap.unLocalizedMapInfo.addInfo(
                ZXMInfo.ObjectCount,
                String.valueOf(
                        Integer.parseInt(srcMap.unLocalizedMapInfo.getInfo(ZXMInfo.ObjectCount)) + count
                )
        );
        //清空添加列表
        if (tempAddList.size() > 0) {
            tempAddList.clear();
        }

        //清除此次编辑产生的所有虚影
        for (int i = 0; i < shadows.size(); i++) {
            shadowMap.deleteNote(shadows.get(i));
        }
        shadows.clear();
        //添加到操作堆栈
        operateStack.add(tempMapOperate);
        if (operateStack.size() > withdrawStackSize)
            operateStack.pollFirst();
        tempMapOperate = null;

    }


    /**
     * 检查操作(检查是否初始化和是否已包含此按键相关操作)
     * 自动处理
     *
     * @param srcNote 要检查的按键
     */
    private FixedOrbitNote checkOperate(FixedOrbitNote srcNote) {
        boolean containsOldNote = false;
        if (tempMapOperate == null) {
            //上一次无操作新建
            tempMapOperate = new MapOperate();
            //加入操作源中
            tempMapOperate.srcNotes.add(srcNote);
        } else if (!tempMapOperate.srcNotes.contains(srcNote)) {

            //上一次有操作,但操作对象不包含传入按键

            //判断是否含有转换前的原按键(长键或滑键)
            if (srcNote instanceof ComplexNote complexNote) {
                if (complexNote.notes.size() != 1) {
                    //长度为1的组合键
                    containsOldNote = true;
                }
            } else {
                //检查操作源是否有同轨同时间按键
                for (BaseNote operateNote : tempMapOperate.srcNotes) {
                    if (operateNote instanceof FixedOrbitNote operateFixedOrbitNote)
                        if (operateFixedOrbitNote.timeStamp == srcNote.timeStamp && operateFixedOrbitNote.orbit == srcNote.orbit) {
                            containsOldNote = true;
                        }
                }
            }
            if (!containsOldNote) {
                //加入操作源中
                tempMapOperate.srcNotes.add(srcNote);
            }

        }
        //克隆获得虚影按键
        FixedOrbitNote shadowNote = srcNote.clone();
        if (!shadows.contains(srcNote)) {
            //将虚影按键加入虚影map中
            shadowMap.insertNote(shadowNote);
            shadows.add(shadowNote);
        } else {


            shadows.remove(srcNote);
            shadows.add(srcNote.clone());
            shadowMap.notes.remove(srcNote);
            shadowMap.insertNote(srcNote.clone());
            return (FixedOrbitNote) shadowMap.notes.get(shadowMap.notes.indexOf(shadowNote));
        }
        return shadowNote;
    }


    /**
     * 检查组合键合法性,自动修正
     *
     * @param note 组合键
     * @param flag 当组合键中只有一个子键时是否转化单键
     */
    private void checkComplexNote(ComplexNote note, boolean flag) {
        //检查按键是否为组合键,修复其中多余节点,0参滑键以及非法参数按键
        ArrayList<FixedOrbitNote> deleteList = new ArrayList<>();
        for (int i = 0; i < note.notes.size(); i++) {
            FixedOrbitNote child = note.notes.get(i);
            if (child instanceof SlideNote slideNote) {
                //检测同时间戳滑键
                //向后检查
                for (int index = i + 1; index < note.notes.size(); index++) {
                    if (note.notes.get(index) instanceof SlideNote nextSlideNote) {
                        //修正误差3ms
                        if (!(Math.abs(slideNote.timeStamp - nextSlideNote.timeStamp) <= 3))
                            break;
                        slideNote.slideArg += nextSlideNote.slideArg;
                        deleteList.add(nextSlideNote);
                    } else {
                        break;
                    }
                }
                //滑子键
                if (slideNote.slideArg == 0) {
                    //将零参滑加入删除列表
                    deleteList.add(slideNote);
                }
            } else if (child instanceof LongNote thisLongNote) {
                //检测同轨长键
                //向后检查
                for (int index = i + 1; index < note.notes.size(); index++) {
                    if (note.notes.get(index) instanceof LongNote nextLongNote) {
                        if (thisLongNote.orbit != nextLongNote.orbit)
                            break;
                        thisLongNote.sustainedTime += nextLongNote.sustainedTime;
                        deleteList.add(nextLongNote);
                    } else {
                        break;
                    }
                }
                if (Math.abs(thisLongNote.sustainedTime) <= 3) {
                    //修正误差3ms
                    deleteList.add(thisLongNote);
                }
            }
        }
        note.notes.removeAll(deleteList);
        if (note.notes.size() == 1 && flag) {
            shadowMap.notes.remove(note);
            shadows.remove(note);
            shadowMap.insertNote(note.notes.get(0).clone());
            shadows.add(note.notes.get(0).clone());
        }
    }

    /**
     * 在指定位置添加物件,确认添加需要done
     * @param timeStamp 时间戳位置
     * @param orbit 轨道位置
     */
    public void addNote(long timeStamp, int orbit) {
        FixedOrbitNote note = new FixedOrbitNote(timeStamp, orbit);
        shadowMap.insertNote(note);
        shadows.add(note);
        if (tempMapOperate == null) {
            tempMapOperate = new MapOperate();
        }
        tempMapOperate.srcNotes.add(null);
    }

    /**
     * 删除物件,确认执行需要done
     * @param srcNote 要删除的物件
     */
    public void deleteNote(FixedOrbitNote srcNote) {
        if (tempMapOperate == null) {
            tempMapOperate = new MapOperate();
        }
        tempMapOperate.srcNotes.add(srcNote);
        tempMapOperate.desNotes.add(null);
    }

    /**
     * 撤回操作
     */
    public void withdraw() {
        if (operateStack.size() > 0) {
            MapOperate operate = operateStack.pollLast();
            for (BaseNote desNote : operate.desNotes) {
                if (desNote != null) {
                    srcMap.unLocalizedMapInfo.addInfo(
                            ZXMInfo.ObjectCount,
                            String.valueOf(
                                    Integer.parseInt(srcMap.unLocalizedMapInfo.getInfo(ZXMInfo.ObjectCount)) - srcMap.deleteNote(desNote)
                            )
                    );
                }
            }
            for (BaseNote srcNote : operate.srcNotes) {
                if (srcNote != null) {
                    srcMap.unLocalizedMapInfo.addInfo(
                            ZXMInfo.ObjectCount,
                            String.valueOf(
                                    Integer.parseInt(srcMap.unLocalizedMapInfo.getInfo(ZXMInfo.ObjectCount)) + srcMap.insertNote(srcNote)
                            )
                    );
                }
            }
            //修改信息
            withdrawStack.add(operate.getReverseOperate());
            if (withdrawStack.size() > 32)
                withdrawStack.pollFirst();
        }
    }

    /**
     * 重做撤回的操作
     */
    public void redo(){

        if (withdrawStack.size() > 0) {
            MapOperate operate = withdrawStack.pollLast();
            for (BaseNote desNote : operate.desNotes) {
                if (desNote != null) {
                    srcMap.unLocalizedMapInfo.addInfo(
                            ZXMInfo.ObjectCount,
                            String.valueOf(
                                    Integer.parseInt(srcMap.unLocalizedMapInfo.getInfo(ZXMInfo.ObjectCount)) - srcMap.deleteNote(desNote)
                            )
                    );
                }
            }
            for (BaseNote srcNote : operate.srcNotes) {
                if (srcNote != null) {
                    srcMap.unLocalizedMapInfo.addInfo(
                            ZXMInfo.ObjectCount,
                            String.valueOf(
                                    Integer.parseInt(srcMap.unLocalizedMapInfo.getInfo(ZXMInfo.ObjectCount)) + srcMap.insertNote(srcNote)
                            )
                    );
                }
            }
            //修改信息
            operateStack.add(operate.getReverseOperate());
            if (operateStack.size() > 32)
                operateStack.pollFirst();
        }
    }


}
