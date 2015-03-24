#!/bin/sh

rm -fr src/main/java-gen/*

mvn exec:exec -P !compile,generate -Dpst_dst=src/main/java-gen -Dpst_source_class=../apache-wave-protobuf/target/classes/org/waveprotocol/box/common/comms/WaveClientRpc.class
mvn exec:exec -P !compile,generate -Dpst_dst=src/main/java-gen -Dpst_source_class=../apache-wave-protobuf/target/classes/org/waveprotocol/box/search/SearchProto.class
mvn exec:exec -P !compile,generate -Dpst_dst=src/main/java-gen -Dpst_source_class=../apache-wave-protobuf/target/classes/org/waveprotocol/box/profile/ProfilesProto.class
mvn exec:exec -P !compile,generate -Dpst_dst=src/main/java-gen -Dpst_source_class=../apache-wave-protobuf/target/classes/org/waveprotocol/box/server/rpc/Rpc.class
mvn exec:exec -P !compile,generate -Dpst_dst=src/main/java-gen -Dpst_source_class=../apache-wave-protobuf/target/classes/org/waveprotocol/box/attachment/AttachmentProto.class
mvn exec:exec -P !compile,generate -Dpst_dst=src/main/java-gen -Dpst_source_class=../apache-wave-protobuf/target/classes/org/waveprotocol/wave/federation/Proto.class
mvn exec:exec -P !compile,generate -Dpst_dst=src/main/java-gen -Dpst_source_class=../apache-wave-protobuf/target/classes/org/waveprotocol/wave/concurrencycontrol/ClientServer.class
mvn exec:exec -P !compile,generate -Dpst_dst=src/main/java-gen -Dpst_source_class=../apache-wave-protobuf/target/classes/org/waveprotocol/wave/diff/Diff.class
