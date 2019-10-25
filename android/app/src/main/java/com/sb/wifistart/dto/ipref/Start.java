package com.sb.wifistart.dto.ipref;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Start {
    ArrayList<Connect> connected = new ArrayList<>();
    private @SerializedName("timestamp") Timestamp timestamp;
    private @SerializedName("connecting_to") ConnectingTo connectingTo;
    private @SerializedName("test_start")  TestStart testStartObject;
    private String version;
    private String system_info;
    private String cookie;
    private float tcp_mss_default;


    // Getter Methods

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSystem_info() {
        return system_info;
    }

    public void setSystem_info(String system_info) {
        this.system_info = system_info;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestampObject) {
        this.timestamp = timestampObject;
    }

    public ConnectingTo getConnecting_to() {
        return connectingTo;
    }

    // Setter Methods

    public void setConnecting_to(ConnectingTo connecting_toObject) {
        this.connectingTo = connecting_toObject;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public float getTcp_mss_default() {
        return tcp_mss_default;
    }

    public void setTcp_mss_default(float tcp_mss_default) {
        this.tcp_mss_default = tcp_mss_default;
    }

    public TestStart getTest_start() {
        return testStartObject;
    }

    public void setTest_start(TestStart test_startObject) {
        this.testStartObject = test_startObject;
    }

    public class Timestamp {
        private String time;
        private float timesecs;


        // Getter Methods

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        // Setter Methods

        public float getTimesecs() {
            return timesecs;
        }

        public void setTimesecs(float timesecs) {
            this.timesecs = timesecs;
        }
    }

    public class ConnectingTo {
        private String host;
        private float port;


        // Getter Methods

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        // Setter Methods

        public float getPort() {
            return port;
        }

        public void setPort(float port) {
            this.port = port;
        }
    }

    public class TestStart {
        private String protocol;
        private float num_streams;
        private float blksize;
        private float omit;
        private float duration;
        private float bytes;
        private float blocks;
        private float reverse;


        // Getter Methods

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public float getNum_streams() {
            return num_streams;
        }

        public void setNum_streams(float num_streams) {
            this.num_streams = num_streams;
        }

        public float getBlksize() {
            return blksize;
        }

        public void setBlksize(float blksize) {
            this.blksize = blksize;
        }

        public float getOmit() {
            return omit;
        }

        public void setOmit(float omit) {
            this.omit = omit;
        }

        // Setter Methods

        public float getDuration() {
            return duration;
        }

        public void setDuration(float duration) {
            this.duration = duration;
        }

        public float getBytes() {
            return bytes;
        }

        public void setBytes(float bytes) {
            this.bytes = bytes;
        }

        public float getBlocks() {
            return blocks;
        }

        public void setBlocks(float blocks) {
            this.blocks = blocks;
        }

        public float getReverse() {
            return reverse;
        }

        public void setReverse(float reverse) {
            this.reverse = reverse;
        }
    }

    public class Connect {
        private float socket;
        private String local_host;
        private float local_port;
        private String remote_host;
        private float remote_port;


        // Getter Methods

        public float getSocket() {
            return socket;
        }

        public String getLocal_host() {
            return local_host;
        }

        public float getLocal_port() {
            return local_port;
        }

        public String getRemote_host() {
            return remote_host;
        }

        public float getRemote_port() {
            return remote_port;
        }

        // Setter Methods

        public void setSocket(float socket) {
            this.socket = socket;
        }

        public void setLocal_host(String local_host) {
            this.local_host = local_host;
        }

        public void setLocal_port(float local_port) {
            this.local_port = local_port;
        }

        public void setRemote_host(String remote_host) {
            this.remote_host = remote_host;
        }

        public void setRemote_port(float remote_port) {
            this.remote_port = remote_port;
        }
    }
}

