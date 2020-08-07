#!/bin/bash


NR_AGENT="newrelic/newrelic.jar"

CATALINA_OPTS="$CATALINA_OPTS -Dnewrelic.config.license_key=$nr_license_key"
CATALINA_OPTS="$CATALINA_OPTS -Dnewrelic.config.log_level=info"
CATALINA_OPTS="$CATALINA_OPTS -Dnewrelic.config.app_name=java-demoapp -Dnewrelic.config.labels='env:box19;location:US'"
CATALINA_OPTS="$CATALINA_OPTS -Dnewrelic.config.distributed_tracing.enabled=true"

#CATALINA_OPTS="$CATALINA_OPTS -Dnewrelic.config.agent_enabled=false "; export CATALINA_OPTS

CATALINA_OPTS="$CATALINA_OPTS -javaagent:$NR_AGENT"; export CATALINA_OPTS

###################

#NEW_RELIC_LICENSE_KEY=; export NEW_RELIC_LICENSE_KEY
#export NEW_RELIC_LABELS="appType:tomcat;appVersion:9"
#export NEW_RELIC_APP_NAME="tomcat200;sampleapp"
#export NEW_RELIC_AGENT_ENABLED="false"

#export NEW_RELIC_EXTENSIONS_CLASS_HISTOGRAM_ENABLED=true
#export NEW_RELIC_EXTENSIONS_CLASS_HISTOGRAM_DELAY_BETWEEN_CALLS_SECONDS=60
#export NEW_RELIC_EXTENSIONS_CLASS_HISTOGRAM_JMAP_PATH=$JAVA_HOME
#export NEW_RELIC_EXTENSIONS_CLASS_HISTOGRAM_CLASSES_PER_HISTOGRAM=100


options=$CATALINA_OPTS

java $options -jar target/spring-boot-hello-world-1.0.0.jar
