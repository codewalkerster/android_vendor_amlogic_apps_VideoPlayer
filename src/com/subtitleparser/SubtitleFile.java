package com.subtitleparser;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import android.util.Log;
import com.subtitleparser.Subtitle;

/**
 * an subtitle file.
 * 
 * @author 
 */
public class SubtitleFile extends LinkedList {

	private float framerate;
	private int curIndex;


	public SubtitleFile() {

		framerate = 0;
		curIndex = 0;
	}

	/**
	 * Remove hearing impaired subs in entire file.
	 * 
	 * @param start
	 *            char (eg. [);
	 * @param end
	 *            char (eg. ]);
	 * @return number of HI subs removed.
	 */
	public int removeHearImp(String start, String end) {
		SubtitleLine tmp = null;
		int removed = 0;
		String s = "";

		for (int i = 0; i < this.size(); i++) {
			tmp = (SubtitleLine) get(i);
			s = Subtitle.removeHearImp(tmp.getText(), start, end);
			if (!s.equals(tmp.getText())) {
				removed++;
				tmp.setText(s);
			}
		}

		return removed;
	}

	/**
	 * Recreate the subtitles numeration to avoid inconsistency.
	 */
	public void reindex() {
		SubtitleLine tmp = null;

		for (int i = 0; i < this.size(); i++) {
			tmp = (SubtitleLine) get(i);
			tmp.setSubN(i + 1);
		}
	}

	/**
	 * Calculate the frame/sec conversion in entire file Needs a positive
	 * framerate.
	 * 
	 */
	public void setAllTimeValues() throws Exception {

		SubtitleLine tmp = null;
		// SubtitleTime begin=null,end=null;

		if (!isValidFramerate())
			throw new Exception(
					"Error::: I need a positive framerate to perform this conversion!");
		for (int i = 0; i < this.size(); i++) {
			tmp = (SubtitleLine) get(i);
			(tmp.getBegin()).setAllValues(getFramerate());
			(tmp.getEnd()).setAllValues(getFramerate());
		}
	}

	/**
	 * Calculate the frame/sec conversion in entire file Needs a positive
	 * framerate.
	 * 
	 */
	public int removeEmptySubs() {
		SubtitleLine tmp = null;
		int removed = 0;

		for (int i = 0; i < this.size(); i++) {
			tmp = (SubtitleLine) get(i);
			if (tmp.isTextEmpty()) {
				this.remove(i);
				removed++;
			}
		}

		return removed;
	}

	/**
	 * Time shift, positive or negative.
	 * 
	 * @param millisec
	 *            milliseconds, positive or negative value;
	 */
	public void timeShiftMil(int millisec) throws Exception {
		SubtitleLine tmp = null;

		for (int i = 0; i < this.size(); i++) {
			tmp = (SubtitleLine) get(i);
			tmp.timeShiftMil(millisec, getFramerate());
		}
	}

	/**
	 * Time shift, positive or negative.
	 * 
	 * @param frames
	 *            milliseconds, positive or negative value;
	 */
	public void timeShiftFr(int frames) throws Exception {
		SubtitleLine tmp = null;

		for (int i = 0; i < this.size(); i++) {
			tmp = (SubtitleLine) get(i);
			tmp.timeShiftFr(frames, getFramerate());
		}
	}

	public void setFramerate(float framerate) {

		this.framerate = framerate;
		SubtitleTime.framerate=framerate;
	}

	public float getFramerate() {
		return framerate;
	}

	public boolean isValidFramerate() {
		if (getFramerate() > 0)
			return true;
		else
			return false;
	}

	public int curSubtitleIndex() {
		return curIndex;
	}

	public SubtitleLine curSubtitle() {
		return (SubtitleLine) get(curIndex);
	}

	

	public void setCurSubtitleIndex(int index) {
		if (index >= 0 && index < size() - 1) {
			curIndex = index;
		}
	}

	public int toNextSubtitle() {
		if (curIndex < size() - 1) {
			curIndex++;
		}
		return curIndex;
	}

	public int toPrevSubtitle() {
		if (curIndex > 0) {
			curIndex--;
		}
		return curIndex;
	}

	public int findSubtitle(int millisec) {

		int ret = -1;
		SubtitleLine p = null;
		SubtitleLine n = null;
		int i;

		p = (SubtitleLine) get(curIndex);
		n = (SubtitleLine) get(curIndex + 1);
		try {
			if (p != null && n != null
					&& millisec >= p.getBegin().getMilValue()
					&& millisec < n.getBegin().getMilValue()) {
				return curIndex;
			}
			if (p != null && millisec >= p.getBegin().getMilValue()
					&& millisec <= p.getEnd().getMilValue()) {
				return curIndex;
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (i = 0; i < size() - 1; i++) {
			p = (SubtitleLine) get(i);
			n = (SubtitleLine) get(i + 1);
			try {
				if (millisec >= p.getBegin().getMilValue()
						&& millisec < n.getBegin().getMilValue()) {
					ret = i;
					break;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			if (i == size() - 1 && millisec >= n.getBegin().getMilValue()
					&& millisec <= n.getEnd().getMilValue()) {
				ret = i;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}

	public int matchSubtitle(int millisec) {
		int ret = -1;
		ret = findSubtitle(millisec);
		if (ret != -1) {
			setCurSubtitleIndex(ret);
		}

		return ret;
	}
	
	

	
	
    public void appendSubtitle(int index, int start, int end, String text) {
    //public int appendSubtitle(int index) {
        
        SubtitleLine sl = null;
        SubtitleTime startTime = null;
        SubtitleTime endTime = null;
        Log.d("subtitleFile", "appendSubtitleFile" + text );

        startTime = new SubtitleTime(start / 3600000, ((start / 1000 ) % 3600) / 60, (start / 1000 ) % 60, start % 1000);
        endTime = new SubtitleTime(end / 3600000, ((end / 1000 ) % 3600) / 60, (end / 1000 ) % 60, end % 1000);

        sl = new SubtitleLine(index, startTime, endTime, text);
        add(sl);
        
        Log.d("subtitleFile", "appendSubtitleFile" + index);
    }

    public void appendSubtitle(int index, int start, int end, byte[] bytearray,String encode) {
        //public int appendSubtitle(int index) {
            SubtitleLine sl = null;
            SubtitleTime startTime = null;
            SubtitleTime endTime = null;
            String text= null;
			try {
				text = new String(bytearray, encode);
			} catch (UnsupportedEncodingException e) {
		        Log.d("subtitleFile", "byte to string err!!!----------");
				e.printStackTrace();
			}

            startTime = new SubtitleTime(start / 3600000, ((start / 1000 ) % 3600) / 60, (start / 1000 ) % 60, start % 1000);
            endTime = new SubtitleTime(end / 3600000, ((end / 1000 ) % 3600) / 60, (end / 1000 ) % 60, end % 1000);

            sl = new SubtitleLine(index, startTime, endTime, text);
            add(sl);
        }    
    
    

}
