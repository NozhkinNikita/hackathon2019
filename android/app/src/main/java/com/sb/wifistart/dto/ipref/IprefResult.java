package com.sb.wifistart.dto.ipref;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class IprefResult {
    private Start start;
    private ArrayList<Interval> intervals = new ArrayList<>();
    private End end;
    private String error;

    // Getter Methods

    public Start getStart() {
        return start;
    }

    public String getError() {
        return error;
    }

    public End getEnd() {
        return end;
    }

    public ArrayList<Interval> getIntervals() {
        return intervals;
    }



    // Setter Methods

    public void setStart(Start startObject) {
        this.start = startObject;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setEnd(End endObject) {
        this.end = endObject;
    }

    public void setIntervals(ArrayList<Interval> intervals) {
        this.intervals = intervals;
    }

    public class Interval {
        private List<Object> streams = new ArrayList<>();
        private @SerializedName("sum") SumInterval sumInterval;

        public List<Object> getStreams() {
            return streams;
        }

        public void setStreams(List<Object> streams) {
            this.streams = streams;
        }

        public SumInterval getSumInterval() {
            return sumInterval;
        }

        public void setSumInterval(SumInterval sumInterval) {
            this.sumInterval = sumInterval;
        }
    }

    public class SumInterval {
        private float start;
        private float end;
        private float seconds;
        private BigDecimal bytes;
        private BigDecimal bits_per_second;
        private BigDecimal retransmits;
        private boolean omitted;


        // Getter Methods

        public float getStart() {
            return start;
        }

        public float getEnd() {
            return end;
        }

        public float getSeconds() {
            return seconds;
        }

        public BigDecimal getBytes() {
            return bytes;
        }

        public BigDecimal getBits_per_second() {
            return bits_per_second;
        }

        public BigDecimal getRetransmits() {
            return retransmits;
        }

        public boolean getOmitted() {
            return omitted;
        }

        // Setter Methods

        public void setStart(float start) {
            this.start = start;
        }

        public void setEnd(float end) {
            this.end = end;
        }

        public void setSeconds(float seconds) {
            this.seconds = seconds;
        }

        public void setBytes(BigDecimal bytes) {
            this.bytes = bytes;
        }

        public void setBits_per_second(BigDecimal bits_per_second) {
            this.bits_per_second = bits_per_second;
        }

        public void setRetransmits(BigDecimal retransmits) {
            this.retransmits = retransmits;
        }

        public void setOmitted(boolean omitted) {
            this.omitted = omitted;
        }
    }
}

