package com.vo.network;

import java.util.List;

/**
 * @author sc
 */
public class ApiEntity {

    private String name;
    private String version;
    private List<PropertySourcesBean> propertySources;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<PropertySourcesBean> getPropertySources() {
        return propertySources;
    }

    public void setPropertySources(List<PropertySourcesBean> propertySources) {
        this.propertySources = propertySources;
    }

    public static class PropertySourcesBean {

        private String name;
        private SourceBean source;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public SourceBean getSource() {
            return source;
        }

        public void setSource(SourceBean source) {
            this.source = source;
        }

        public static class SourceBean {

            private String push;
            private String api;

            public String getPush() {
                return push;
            }

            public void setPush(String push) {
                this.push = push;
            }

            public String getApi() {
                return api;
            }

            public void setApi(String api) {
                this.api = api;
            }
        }
    }
}
