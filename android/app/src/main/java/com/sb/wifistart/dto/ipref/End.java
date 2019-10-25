package com.sb.wifistart.dto.ipref;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class End {
    //    private ArrayList<Object> streams = new ArrayList<Object>();
    private @SerializedName("sum_sent") SumSent sumSentObject;
    private @SerializedName("sum_received") SumReceived sumReceivedObject;
//    private @SerializedName("cpu_utilization_percent") CpuUtilizationPercent cpuUtilizationPercentObject;

    // Getter Methods

    public SumSent getSum_sent() {
        return sumSentObject;
    }

    public void setSum_sent(SumSent sum_sentObject) {
        this.sumSentObject = sum_sentObject;
    }

    public SumReceived getSum_received() {
        return sumReceivedObject;
    }

    // Setter Methods

    public void setSum_received(SumReceived sum_receivedObject) {
        this.sumReceivedObject = sum_receivedObject;
    }

//    public CpuUtilizationPercent getCpu_utilization_percent() {
//        return cpuUtilizationPercentObject;
//    }

//    public void setCpu_utilization_percent(CpuUtilizationPercent cpu_utilizationPercentObject) {
//        this.cpuUtilizationPercentObject = cpu_utilizationPercentObject;
//    }

    public class SumSent {
        private float start;
        private float end;
        private float seconds;
        private BigDecimal bytes;
        private BigDecimal bits_per_second;
        private BigDecimal retransmits;


        // Getter Methods

        public float getStart() {
            return start;
        }

        public void setStart(float start) {
            this.start = start;
        }

        public float getEnd() {
            return end;
        }

        public void setEnd(float end) {
            this.end = end;
        }

        public float getSeconds() {
            return seconds;
        }

        public void setSeconds(float seconds) {
            this.seconds = seconds;
        }

        // Setter Methods

        public BigDecimal getBytes() {
            return bytes;
        }

        public void setBytes(BigDecimal bytes) {
            this.bytes = bytes;
        }

        public BigDecimal getBits_per_second() {
            return bits_per_second;
        }

        public void setBits_per_second(BigDecimal bits_per_second) {
            this.bits_per_second = bits_per_second;
        }

        public BigDecimal getRetransmits() {
            return retransmits;
        }

        public void setRetransmits(BigDecimal retransmits) {
            this.retransmits = retransmits;
        }
    }

    public class SumReceived {
        private float start;
        private float end;
        private float seconds;
        private BigDecimal bytes;
        private BigDecimal bits_per_second;


        // Getter Methods

        public float getStart() {
            return start;
        }

        public void setStart(float start) {
            this.start = start;
        }

        public float getEnd() {
            return end;
        }

        public void setEnd(float end) {
            this.end = end;
        }

        public float getSeconds() {
            return seconds;
        }

        // Setter Methods

        public void setSeconds(float seconds) {
            this.seconds = seconds;
        }

        public BigDecimal getBytes() {
            return bytes;
        }

        public void setBytes(BigDecimal bytes) {
            this.bytes = bytes;
        }

        public BigDecimal getBits_per_second() {
            return bits_per_second;
        }

        public void setBits_per_second(BigDecimal bits_per_second) {
            this.bits_per_second = bits_per_second;
        }
    }

    public class CpuUtilizationPercent {
        private float host_total;
        private float host_user;
        private float host_system;
        private float remote_total;
        private float remote_user;
        private float remote_system;


        // Getter Methods

        public float getHost_total() {
            return host_total;
        }

        public void setHost_total(float host_total) {
            this.host_total = host_total;
        }

        public float getHost_user() {
            return host_user;
        }

        public void setHost_user(float host_user) {
            this.host_user = host_user;
        }

        public float getHost_system() {
            return host_system;
        }

        public void setHost_system(float host_system) {
            this.host_system = host_system;
        }

        // Setter Methods

        public float getRemote_total() {
            return remote_total;
        }

        public void setRemote_total(float remote_total) {
            this.remote_total = remote_total;
        }

        public float getRemote_user() {
            return remote_user;
        }

        public void setRemote_user(float remote_user) {
            this.remote_user = remote_user;
        }

        public float getRemote_system() {
            return remote_system;
        }

        public void setRemote_system(float remote_system) {
            this.remote_system = remote_system;
        }
    }
}
