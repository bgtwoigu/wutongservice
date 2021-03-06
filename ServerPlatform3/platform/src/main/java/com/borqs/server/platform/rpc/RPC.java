/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.borqs.server.platform.rpc;

@SuppressWarnings("all")
public interface RPC {
  public static final org.apache.avro.Protocol PROTOCOL = org.apache.avro.Protocol.parse("{\"protocol\":\"RPC\",\"namespace\":\"com.borqs.server.platform.rpc.avro\",\"types\":[{\"type\":\"error\",\"name\":\"RPCError\",\"fields\":[{\"name\":\"code\",\"type\":\"int\"},{\"name\":\"message\",\"type\":\"string\"}]}],\"messages\":{\"rpc\":{\"request\":[{\"name\":\"category\",\"type\":\"string\"},{\"name\":\"method\",\"type\":\"string\"},{\"name\":\"args\",\"type\":{\"type\":\"array\",\"items\":\"bytes\"}}],\"response\":\"bytes\",\"errors\":[\"RPCError\"]}}}");
  java.nio.ByteBuffer rpc(java.lang.CharSequence category, java.lang.CharSequence method, java.util.List<java.nio.ByteBuffer> args) throws org.apache.avro.AvroRemoteException, RPCError;

  @SuppressWarnings("all")
  public interface Callback extends RPC {
    public static final org.apache.avro.Protocol PROTOCOL = RPC.PROTOCOL;
    void rpc(java.lang.CharSequence category, java.lang.CharSequence method, java.util.List<java.nio.ByteBuffer> args, org.apache.avro.ipc.Callback<java.nio.ByteBuffer> callback) throws java.io.IOException;
  }
}