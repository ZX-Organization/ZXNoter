package team.zxorg.zxnoter.note.fixedorbit;

import team.zxorg.zxnoter.map.mapInfos.ImdInfo;
import team.zxorg.zxnoter.note.BaseNote;

import java.util.ArrayList;
import java.util.Arrays;

public class ComplexNote extends FixedOrbitNote implements Cloneable{
    public ArrayList<FixedOrbitNote> notes = new ArrayList<>();
    public ComplexNote(long timeStamp, int orbit, ArrayList<FixedOrbitNote> notes) {
        super(timeStamp, orbit);
        this.notes = notes;
    }
    public ComplexNote(long timeStamp, int orbit) {
        super(timeStamp, orbit);
    }


    public void addNote(FixedOrbitNote note){
        notes.add(note);
    }
    public void clearNote(){
        notes.clear();
        timeStamp = 0;
        orbit = 0;
    }

    /**
     * 将imd组合键转化为常规4k键
     * @param convertMethod 转换方式
     * @return 转化结果数组
     */
    public ArrayList<FixedOrbitNote> convertNote(ImdInfo.ConvertMethod convertMethod){
        switch (convertMethod){
            case BASE -> {
                ArrayList<FixedOrbitNote> convertNotes = new ArrayList<>();
                convertNotes.add(new FixedOrbitNote(timeStamp,orbit)) ;
                return convertNotes;
            }
            case BASE_SLIDE ->{
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
     * @param isAdvSlideCon 是否使用高级滑键转换
     * @return 转换结果数组
     */
    private ArrayList<FixedOrbitNote> convertCom(boolean isAdvSlideCon){
        ArrayList<FixedOrbitNote> convertNotes = new ArrayList<>();

        //判断首端是否为滑键
        if (notes.get(0) instanceof SlideNote head){
            //判断尾端是否为箭头(滑键)
            if (notes.get(notes.size()-1) instanceof SlideNote end){
                int index = 0;
                for (int i = 0; i <= Math.abs(head.slideArg) - 1; i++) {
                    //头部滑键
                    if (isAdvSlideCon){
                        //高级滑键转换
                        convertNotes.add( new FixedOrbitNote(
                                head.timeStamp + (int)(i*((double)25 / -(Math.abs(head.slideArg) - 1))),
                                head.orbit + (head.slideArg > 0 ? i : -i)
                        ));
                    }else {
                        //正常滑键转换
                        convertNotes.add(new FixedOrbitNote(head.timeStamp, head.orbit + (head.slideArg > 0 ? i : -i)));
                    }
                }
                for (int i = 1; i <= notes.size()-1; i++) {
                    //组合键中间部分
                    if (notes.get(i) instanceof LongNote longNote){
                        //长条
                        convertNotes.add(new LongNote(longNote.timeStamp, longNote.orbit, longNote.sustainedTime));
                    }else if (notes.get(i) instanceof SlideNote slideNote){
                        if (Math.abs(slideNote.slideArg) > 1){
                            //中间超过一轨滑,掐头去尾,中间填单键
                            for (int j = 1; j < Math.abs(slideNote.slideArg) - 1; j++) {
                                convertNotes.add(new FixedOrbitNote(slideNote.timeStamp,slideNote.orbit +(slideNote.slideArg > 0 ? i : -i)));
                            }
                        }
                    }
                }
                //结尾滑键
                for (int i = 1; i <= Math.abs(end.slideArg); i++) {
                    if(isAdvSlideCon){
                        convertNotes.add(new FixedOrbitNote(
                                end.timeStamp+ (int)(i * ((double)25 / (Math.abs(end.slideArg) + 1))),
                                end.orbit + (end.slideArg > 0 ? i : -i)
                        ));
                    }else {
                        convertNotes.add(new FixedOrbitNote(end.timeStamp, end.orbit + (end.slideArg > 0 ? i : -i)));
                    }
                }
                return convertNotes;
            }else {
                //面条结尾
                int index = 0;
                for (int i = 0; i <= Math.abs(head.slideArg) - 1; i++) {
                    if (isAdvSlideCon){
                        //高级滑键转换
                        convertNotes.add(new FixedOrbitNote(
                                head.timeStamp+(int)(i*((double)25 / -(Math.abs(head.slideArg) - 1))),
                                head.orbit+orbit + (head.slideArg > 0 ? i : -i)
                        )) ;
                    }else {
                        //正常滑键转换
                        convertNotes.add(new FixedOrbitNote(head.timeStamp, head.orbit + (head.slideArg > 0 ? i : -i)));
                    }
                }
                for (int i = 1; i <= notes.size()-1; i++) {
                    //组合键中间部分
                    if (notes.get(i) instanceof LongNote longNote){
                        //长条
                        convertNotes.add(new LongNote(longNote.timeStamp, longNote.orbit, longNote.sustainedTime));
                    }else if (notes.get(i) instanceof SlideNote slideNote){
                        if (Math.abs(slideNote.slideArg) > 1){
                            //中间超过一轨滑,掐头去尾,中间填单键
                            for (int j = 1; j <= Math.abs(slideNote.slideArg) - 1; j++) {
                                convertNotes.add(new FixedOrbitNote(slideNote.timeStamp,slideNote.orbit +(slideNote.slideArg > 0 ? i : -i)));
                            }
                        }
                    }
                }

                return convertNotes;
            }
        }else {
            //判断尾端是否为箭头(滑键)
            if (notes.get(notes.size()-1) instanceof SlideNote end){
                int index = 0 ;
                for (int i = 0 ; i < notes.size()-1; i++) {
                    //组合键中间部分
                    if (notes.get(i) instanceof LongNote longNote){
                        //长条
                        convertNotes.add(new LongNote(longNote.timeStamp, longNote.orbit, longNote.sustainedTime));
                        index++;
                    }else if (notes.get(i) instanceof SlideNote slideNote){
                        if (Math.abs(slideNote.slideArg) > 1){
                            //中间超过一轨滑,掐头去尾,中间填单键
                            for (int j = 1; j <= Math.abs(slideNote.slideArg) - 1; j++) {
                                convertNotes.add(new FixedOrbitNote(slideNote.timeStamp,slideNote.orbit +(slideNote.slideArg > 0 ? i : -i)));
                            }
                        }
                    }
                }
                //结尾滑键
                for (int i = 1; i <= Math.abs(end.slideArg) ; i++) {
                    if (isAdvSlideCon){
                        convertNotes.add(new FixedOrbitNote(
                                end.timeStamp+ (int)(i * ((double)25 / (Math.abs(end.slideArg) + 1))),
                                end.orbit + (end.slideArg > 0 ? i : -i)
                        ));
                    }else {
                        convertNotes.add(new FixedOrbitNote(end.timeStamp, end.orbit + (end.slideArg > 0 ? i : -i)));
                    }
                    index++;
                }
                return convertNotes;
            }else {
                //面条结尾
                int index = 0 ;
                for (int i = 0 ; i <= notes.size()-1; i++) {
                    //组合键中间部分
                    if (notes.get(i) instanceof LongNote longNote){
                        //长条
                        convertNotes.add(new LongNote(longNote.timeStamp, longNote.orbit, longNote.sustainedTime));
                    }else if (notes.get(i) instanceof SlideNote slideNote){
                        if (Math.abs(slideNote.slideArg) > 1){
                            //中间超过一轨滑,掐头去尾,中间填单键
                            for (int j = 1; j < Math.abs(slideNote.slideArg) - 1; j++) {
                                convertNotes.add(new FixedOrbitNote(slideNote.timeStamp,slideNote.orbit +(slideNote.slideArg > 0 ? i : -i)));
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
        ArrayList<FixedOrbitNote> newNotes = new ArrayList<>();
        for (int i = 0; i < notes.size(); i++) {
            newNotes.add(notes.get(i).clone());
        }
        return new ComplexNote(timeStamp,orbit,newNotes);
    }

    @Override
    public long getLength() {
        long length=0;
        for (FixedOrbitNote fixedOrbitNote:notes){
            if (fixedOrbitNote instanceof LongNote longNote){
                length+= longNote.sustainedTime;
            }
        }
        return length;
    }


    @Override
    public String toString() {
        return '\n' +"ComplexNote{" +
                "组合列表=" + notes +'\n' +
                ", 起始轨道=" + orbit +
                ", 起始时间=" + timeStamp +
                '}'+'\n';
    }
}
