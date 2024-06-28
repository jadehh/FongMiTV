package com.fongmi.android.tv.bean;

import android.text.TextUtils;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.Collections;
import java.util.List;

@Root(name = "tv", strict = false)
public class Tv {

    @ElementList(entry = "channel", required = false, inline = true)
    private List<Channel> channel;

    @ElementList(entry = "programme", required = false, inline = true)
    private List<Programme> programme;

    public List<Channel> getChannel() {
        return channel == null ? Collections.emptyList() : channel;
    }

    public List<Programme> getProgramme() {
        return programme == null ? Collections.emptyList() : programme;
    }

    @Root(name = "channel")
    public static class Channel {

        @Attribute(name = "id", required = false)
        private String id;

        @Element(name = "display-name", required = false)
        private String displayName;

        public String getId() {
            return TextUtils.isEmpty(id) ? "" : id;
        }

        public String getDisplayName() {
            return TextUtils.isEmpty(displayName) ? "" : displayName;
        }
    }

    @Root(name = "programme")
    public static class Programme {

        @Attribute(name = "start", required = false)
        private String start;

        @Attribute(name = "stop", required = false)
        private String stop;

        @Attribute(name = "channel", required = false)
        private String channel;

        @Element(name = "title", required = false)
        private String title;

        public String getStart() {
            return TextUtils.isEmpty(start) ? "" : start;
        }

        public String getStop() {
            return TextUtils.isEmpty(stop) ? "" : stop;
        }

        public String getChannel() {
            return TextUtils.isEmpty(channel) ? "" : channel;
        }

        public String getTitle() {
            return TextUtils.isEmpty(title) ? "" : title;
        }
    }
}
