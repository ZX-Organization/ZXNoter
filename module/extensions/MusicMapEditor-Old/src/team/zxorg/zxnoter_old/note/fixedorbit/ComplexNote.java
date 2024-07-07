package team.zxorg.zxnoter_old.note.fixedorbit;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import team.zxorg.zxnoter_old.info.map.ImdInfo;

import java.util.ArrayList;

public class ComplexNote extends FixedOrbitNote implements Cloneable {
    public ArrayList<FixedOrbitNote> notes = new ArrayList<>();
    private boolean isRelativeHead;

    public ComplexNote(long timeStamp, int orbit, ArrayList<FixedOrbitNote> notes,boolean isRelativeHead) {
        super(timeStamp, orbit);
        this.notes = notes;
        this.isRelativeHead = isRelativeHead;
    }
    public ComplexNote(JSONObject noteJson) {
        super(noteJson.getLongValue("time"), noteJson.getIntValue("orbit"),noteJson.getString("soundPath"));
        JSONArray children = noteJson.getJSONArray("child");
        notes = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            JSONObject childJson = children.getJSONObject(i);
            if (childJson.size()==3){
                //普通单键
                notes.add(new FixedOrbitNote(childJson));
            }
            if (childJson.containsKey("slideArg")){
                //滑键
                notes.add(new SlideNote(childJson));
            }
            if (childJson.containsKey("sustainedTime")){
                //长条
                //判断是否为特殊长条
                if (childJson.size() == 9) notes.add(new CustomLongNote(childJson));
                else notes.add(new LongNote(childJson));
            }
            if (childJson.size()==8){
                //特殊单键
                notes.add(new CustomNote(childJson));
            }
        }

        isRelativeHead = noteJson.getBooleanValue("isRelativeHead");
    }

    public ComplexNote(ComplexNote complexNote) {
        super(complexNote.timeStamp, complexNote.orbit);
        for (int i = 0; i < complexNote.notes.size(); i++) {
            notes.add(complexNote.notes.get(i).clone());
        }
        isRelativeHead = complexNote.isRelativeHead;
        hash = complexNote.hash;
    }

    public ComplexNote(long timeStamp, int orbit) {
        super(timeStamp, orbit);
    }


    public void addNote(FixedOrbitNote note) {
        notes.add(note);
    }

    public void clearNote() {
        notes.clear();
        timeStamp = 0;
        orbit = 0;
    }

    public boolean isRelativeHead() {
        return isRelativeHead;
    }

    public void setRelatively(boolean relativeHead) {
        if (isRelativeHead == relativeHead) return;
        else {
            FixedOrbitNote head = notes.get(0);
            if (relativeHead) {
                //设置为相对头部
                for (int i = 1; i < notes.size(); i++) {
                    //计算相对时间戳
                    notes.get(i).timeStamp = notes.get(i).timeStamp - head.timeStamp;
                    //计算相对轨道
                    notes.get(i).orbit = notes.get(i).orbit - head.orbit;
                }
            } else {
                //设置为绝对
                for (int i = 1; i < notes.size(); i++) {
                    //计算绝对时间戳
                    notes.get(i).timeStamp = notes.get(i).timeStamp + head.timeStamp;
                    //计算绝对轨道
                    notes.get(i).orbit = notes.get(i).orbit + head.orbit;
                }
            }
        }
        isRelativeHead = relativeHead;
    }

    /**
     * 将imd组合键转化为常规4k键
     *
     * @param convertMethod 转换方式
     * @return 转化结果数组
     */
    public ArrayList<FixedOrbitNote> convertNote(ImdInfo.ConvertMethod convertMethod) {
        switch (convertMethod) {
            case BASE -> {
                ArrayList<FixedOrbitNote> convertNotes = new ArrayList<>();
                convertNotes.add(new FixedOrbitNote(timeStamp, orbit));
                return convertNotes;
            }
            case BASE_SLIDE -> {
                return convertCom(false);
            }
            case ADVANCE_SLIDE -> {
                return convertCom(true);
            }
        }
        return null;
    }

    /**
     * 私有转换组合键方法(臭的一比,但已经干活了,不要动它,不要展开)
     *
     * @param isAdvSlideCon 是否使用高级滑键转换
     * @return 转换结果数组
     */
    private ArrayList<FixedOrbitNote> convertCom(boolean isAdvSlideCon) {
        ArrayList<FixedOrbitNote> convertNotes = new ArrayList<>();

        //判断首端是否为滑键
        if (notes.get(0) instanceof SlideNote head) {
            //判断尾端是否为箭头(滑键)
            if (notes.get(notes.size() - 1) instanceof SlideNote end) {
                int index = 0;
                for (int i = 0; i <= Math.abs(head.slideArg) - 1; i++) {
                    //头部滑键
                    if (isAdvSlideCon) {
                        //高级滑键转换
                        convertNotes.add(new FixedOrbitNote(
                                head.timeStamp + (int) (i * ((double) 25 / -(Math.abs(head.slideArg) - 1))),
                                head.orbit + (head.slideArg > 0 ? i : -i)
                        ));
                    } else {
                        //正常滑键转换
                        convertNotes.add(new FixedOrbitNote(head.timeStamp, head.orbit + (head.slideArg > 0 ? i : -i)));
                    }
                }
                for (int i = 1; i <= notes.size() - 1; i++) {
                    //组合键中间部分
                    if (notes.get(i) instanceof LongNote longNote) {
                        //长条
                        convertNotes.add(new LongNote(longNote.timeStamp, longNote.orbit, longNote.sustainedTime));
                    } else if (notes.get(i) instanceof SlideNote slideNote) {
                        if (Math.abs(slideNote.slideArg) > 1) {
                            //中间超过一轨滑,掐头去尾,中间填单键
                            for (int j = 1; j < Math.abs(slideNote.slideArg) - 1; j++) {
                                convertNotes.add(new FixedOrbitNote(slideNote.timeStamp, slideNote.orbit + (slideNote.slideArg > 0 ? i : -i)));
                            }
                        }
                    }
                }
                //结尾滑键
                for (int i = 1; i <= Math.abs(end.slideArg); i++) {
                    if (isAdvSlideCon) {
                        convertNotes.add(new FixedOrbitNote(
                                end.timeStamp + (int) (i * ((double) 25 / (Math.abs(end.slideArg) + 1))),
                                end.orbit + (end.slideArg > 0 ? i : -i)
                        ));
                    } else {
                        convertNotes.add(new FixedOrbitNote(end.timeStamp, end.orbit + (end.slideArg > 0 ? i : -i)));
                    }
                }
                return convertNotes;
            } else {
                //面条结尾
                int index = 0;
                for (int i = 0; i <= Math.abs(head.slideArg) - 1; i++) {
                    if (isAdvSlideCon) {
                        //高级滑键转换
                        convertNotes.add(new FixedOrbitNote(
                                head.timeStamp + (int) (i * ((double) 25 / -(Math.abs(head.slideArg) - 1))),
                                head.orbit + orbit + (head.slideArg > 0 ? i : -i)
                        ));
                    } else {
                        //正常滑键转换
                        convertNotes.add(new FixedOrbitNote(head.timeStamp, head.orbit + (head.slideArg > 0 ? i : -i)));
                    }
                }
                for (int i = 1; i <= notes.size() - 1; i++) {
                    //组合键中间部分
                    if (notes.get(i) instanceof LongNote longNote) {
                        //长条
                        convertNotes.add(new LongNote(longNote.timeStamp, longNote.orbit, longNote.sustainedTime));
                    } else if (notes.get(i) instanceof SlideNote slideNote) {
                        if (Math.abs(slideNote.slideArg) > 1) {
                            //中间超过一轨滑,掐头去尾,中间填单键
                            for (int j = 1; j <= Math.abs(slideNote.slideArg) - 1; j++) {
                                convertNotes.add(new FixedOrbitNote(slideNote.timeStamp, slideNote.orbit + (slideNote.slideArg > 0 ? i : -i)));
                            }
                        }
                    }
                }

                return convertNotes;
            }
        } else {
            //判断尾端是否为箭头(滑键)
            if (notes.get(notes.size() - 1) instanceof SlideNote end) {
                int index = 0;
                for (int i = 0; i < notes.size() - 1; i++) {
                    //组合键中间部分
                    if (notes.get(i) instanceof LongNote longNote) {
                        //长条
                        convertNotes.add(new LongNote(longNote.timeStamp, longNote.orbit, longNote.sustainedTime));
                        index++;
                    } else if (notes.get(i) instanceof SlideNote slideNote) {
                        if (Math.abs(slideNote.slideArg) > 1) {
                            //中间超过一轨滑,掐头去尾,中间填单键
                            for (int j = 1; j <= Math.abs(slideNote.slideArg) - 1; j++) {
                                convertNotes.add(new FixedOrbitNote(slideNote.timeStamp, slideNote.orbit + (slideNote.slideArg > 0 ? i : -i)));
                            }
                        }
                    }
                }
                //结尾滑键
                for (int i = 1; i <= Math.abs(end.slideArg); i++) {
                    if (isAdvSlideCon) {
                        convertNotes.add(new FixedOrbitNote(
                                end.timeStamp + (int) (i * ((double) 25 / (Math.abs(end.slideArg) + 1))),
                                end.orbit + (end.slideArg > 0 ? i : -i)
                        ));
                    } else {
                        convertNotes.add(new FixedOrbitNote(end.timeStamp, end.orbit + (end.slideArg > 0 ? i : -i)));
                    }
                    index++;
                }
                return convertNotes;
            } else {
                //面条结尾
                int index = 0;
                for (int i = 0; i <= notes.size() - 1; i++) {
                    //组合键中间部分
                    if (notes.get(i) instanceof LongNote longNote) {
                        //长条
                        convertNotes.add(new LongNote(longNote.timeStamp, longNote.orbit, longNote.sustainedTime));
                    } else if (notes.get(i) instanceof SlideNote slideNote) {
                        if (Math.abs(slideNote.slideArg) > 1) {
                            //中间超过一轨滑,掐头去尾,中间填单键
                            for (int j = 1; j < Math.abs(slideNote.slideArg) - 1; j++) {
                                convertNotes.add(new FixedOrbitNote(slideNote.timeStamp, slideNote.orbit + (slideNote.slideArg > 0 ? i : -i)));
                            }
                        }
                    }
                }
                return convertNotes;
            }
        }
    }

    @Override
    public ComplexNote clone() {
        return new ComplexNote(this);
    }

    @Override
    public long getLength() {
        long length = 0;
        for (FixedOrbitNote fixedOrbitNote : notes) {
            if (fixedOrbitNote instanceof LongNote longNote) {
                length += longNote.sustainedTime;
            }
        }
        return length;
    }

    @Override
    public JSONObject toJson() {
        JSONObject noteJson = new JSONObject();
        noteJson.put("time", timeStamp);
        noteJson.put("orbit", orbit);
        noteJson.put("soundKey",soundKey);
        noteJson.put("soundPath",soundPath);
        noteJson.put("isRelativeHead",isRelativeHead);
        JSONArray childrenNotes = new JSONArray();
        for (FixedOrbitNote fixedOrbitNote:notes)
            childrenNotes.add(fixedOrbitNote.toJson());
        noteJson.put("child",childrenNotes);
        return noteJson;
    }
    @Override
    public ComplexNote getParent() {
        return this;
    }


    @Override
    public String toString() {
        return '\n' + "ComplexNote{" +
                "组合列表=" + notes + '\n' +
                ", 起始轨道=" + orbit +
                ", 起始时间=" + timeStamp +
                ", 相对组合头=" + isRelativeHead +
                '}' + '\n';
    }
}
