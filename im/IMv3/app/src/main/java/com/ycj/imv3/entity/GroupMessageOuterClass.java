// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: group_message.proto

package com.ycj.imv3.entity;

public final class GroupMessageOuterClass {
  private GroupMessageOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface GroupMessageOrBuilder extends
      // @@protoc_insertion_point(interface_extends:com.imv3.GroupMessage)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>int64 from = 1;</code>
     * @return The from.
     */
    long getFrom();

    /**
     * <code>int64 to = 2;</code>
     * @return The to.
     */
    long getTo();

    /**
     * <code>string content = 3;</code>
     * @return The content.
     */
    String getContent();
    /**
     * <code>string content = 3;</code>
     * @return The bytes for content.
     */
    com.google.protobuf.ByteString
        getContentBytes();

    /**
     * <code>int64 timestamp = 4;</code>
     * @return The timestamp.
     */
    long getTimestamp();
  }
  /**
   * Protobuf type {@code com.imv3.GroupMessage}
   */
  public static final class GroupMessage extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:com.imv3.GroupMessage)
      GroupMessageOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use GroupMessage.newBuilder() to construct.
    private GroupMessage(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private GroupMessage() {
      content_ = "";
    }

    @Override
    @SuppressWarnings({"unused"})
    protected Object newInstance(
        UnusedPrivateParameter unused) {
      return new GroupMessage();
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private GroupMessage(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
        throw new NullPointerException();
      }
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 8: {

              from_ = input.readInt64();
              break;
            }
            case 16: {

              to_ = input.readInt64();
              break;
            }
            case 26: {
              String s = input.readStringRequireUtf8();

              content_ = s;
              break;
            }
            case 32: {

              timestamp_ = input.readInt64();
              break;
            }
            default: {
              if (!parseUnknownField(
                  input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return GroupMessageOuterClass.internal_static_com_imv3_GroupMessage_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return GroupMessageOuterClass.internal_static_com_imv3_GroupMessage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              GroupMessage.class, Builder.class);
    }

    public static final int FROM_FIELD_NUMBER = 1;
    private long from_;
    /**
     * <code>int64 from = 1;</code>
     * @return The from.
     */
    @Override
    public long getFrom() {
      return from_;
    }

    public static final int TO_FIELD_NUMBER = 2;
    private long to_;
    /**
     * <code>int64 to = 2;</code>
     * @return The to.
     */
    @Override
    public long getTo() {
      return to_;
    }

    public static final int CONTENT_FIELD_NUMBER = 3;
    private volatile Object content_;
    /**
     * <code>string content = 3;</code>
     * @return The content.
     */
    @Override
    public String getContent() {
      Object ref = content_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        content_ = s;
        return s;
      }
    }
    /**
     * <code>string content = 3;</code>
     * @return The bytes for content.
     */
    @Override
    public com.google.protobuf.ByteString
        getContentBytes() {
      Object ref = content_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        content_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int TIMESTAMP_FIELD_NUMBER = 4;
    private long timestamp_;
    /**
     * <code>int64 timestamp = 4;</code>
     * @return The timestamp.
     */
    @Override
    public long getTimestamp() {
      return timestamp_;
    }

    private byte memoizedIsInitialized = -1;
    @Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (from_ != 0L) {
        output.writeInt64(1, from_);
      }
      if (to_ != 0L) {
        output.writeInt64(2, to_);
      }
      if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(content_)) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 3, content_);
      }
      if (timestamp_ != 0L) {
        output.writeInt64(4, timestamp_);
      }
      unknownFields.writeTo(output);
    }

    @Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (from_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(1, from_);
      }
      if (to_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(2, to_);
      }
      if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(content_)) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, content_);
      }
      if (timestamp_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(4, timestamp_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof GroupMessage)) {
        return super.equals(obj);
      }
      GroupMessage other = (GroupMessage) obj;

      if (getFrom()
          != other.getFrom()) return false;
      if (getTo()
          != other.getTo()) return false;
      if (!getContent()
          .equals(other.getContent())) return false;
      if (getTimestamp()
          != other.getTimestamp()) return false;
      if (!unknownFields.equals(other.unknownFields)) return false;
      return true;
    }

    @Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + FROM_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getFrom());
      hash = (37 * hash) + TO_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getTo());
      hash = (37 * hash) + CONTENT_FIELD_NUMBER;
      hash = (53 * hash) + getContent().hashCode();
      hash = (37 * hash) + TIMESTAMP_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getTimestamp());
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static GroupMessage parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static GroupMessage parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static GroupMessage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static GroupMessage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static GroupMessage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static GroupMessage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static GroupMessage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static GroupMessage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static GroupMessage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static GroupMessage parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static GroupMessage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static GroupMessage parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(GroupMessage prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @Override
    protected Builder newBuilderForType(
        BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code com.imv3.GroupMessage}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:com.imv3.GroupMessage)
        GroupMessageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return GroupMessageOuterClass.internal_static_com_imv3_GroupMessage_descriptor;
      }

      @Override
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return GroupMessageOuterClass.internal_static_com_imv3_GroupMessage_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                GroupMessage.class, Builder.class);
      }

      // Construct using com.ycj.imv3.entity.GroupMessageOuterClass.GroupMessage.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      @Override
      public Builder clear() {
        super.clear();
        from_ = 0L;

        to_ = 0L;

        content_ = "";

        timestamp_ = 0L;

        return this;
      }

      @Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return GroupMessageOuterClass.internal_static_com_imv3_GroupMessage_descriptor;
      }

      @Override
      public GroupMessage getDefaultInstanceForType() {
        return GroupMessage.getDefaultInstance();
      }

      @Override
      public GroupMessage build() {
        GroupMessage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @Override
      public GroupMessage buildPartial() {
        GroupMessage result = new GroupMessage(this);
        result.from_ = from_;
        result.to_ = to_;
        result.content_ = content_;
        result.timestamp_ = timestamp_;
        onBuilt();
        return result;
      }

      @Override
      public Builder clone() {
        return super.clone();
      }
      @Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return super.setField(field, value);
      }
      @Override
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }
      @Override
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }
      @Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return super.setRepeatedField(field, index, value);
      }
      @Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return super.addRepeatedField(field, value);
      }
      @Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof GroupMessage) {
          return mergeFrom((GroupMessage)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(GroupMessage other) {
        if (other == GroupMessage.getDefaultInstance()) return this;
        if (other.getFrom() != 0L) {
          setFrom(other.getFrom());
        }
        if (other.getTo() != 0L) {
          setTo(other.getTo());
        }
        if (!other.getContent().isEmpty()) {
          content_ = other.content_;
          onChanged();
        }
        if (other.getTimestamp() != 0L) {
          setTimestamp(other.getTimestamp());
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      @Override
      public final boolean isInitialized() {
        return true;
      }

      @Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        GroupMessage parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (GroupMessage) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private long from_ ;
      /**
       * <code>int64 from = 1;</code>
       * @return The from.
       */
      @Override
      public long getFrom() {
        return from_;
      }
      /**
       * <code>int64 from = 1;</code>
       * @param value The from to set.
       * @return This builder for chaining.
       */
      public Builder setFrom(long value) {
        
        from_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int64 from = 1;</code>
       * @return This builder for chaining.
       */
      public Builder clearFrom() {
        
        from_ = 0L;
        onChanged();
        return this;
      }

      private long to_ ;
      /**
       * <code>int64 to = 2;</code>
       * @return The to.
       */
      @Override
      public long getTo() {
        return to_;
      }
      /**
       * <code>int64 to = 2;</code>
       * @param value The to to set.
       * @return This builder for chaining.
       */
      public Builder setTo(long value) {
        
        to_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int64 to = 2;</code>
       * @return This builder for chaining.
       */
      public Builder clearTo() {
        
        to_ = 0L;
        onChanged();
        return this;
      }

      private Object content_ = "";
      /**
       * <code>string content = 3;</code>
       * @return The content.
       */
      public String getContent() {
        Object ref = content_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          content_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <code>string content = 3;</code>
       * @return The bytes for content.
       */
      public com.google.protobuf.ByteString
          getContentBytes() {
        Object ref = content_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          content_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string content = 3;</code>
       * @param value The content to set.
       * @return This builder for chaining.
       */
      public Builder setContent(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        content_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>string content = 3;</code>
       * @return This builder for chaining.
       */
      public Builder clearContent() {
        
        content_ = getDefaultInstance().getContent();
        onChanged();
        return this;
      }
      /**
       * <code>string content = 3;</code>
       * @param value The bytes for content to set.
       * @return This builder for chaining.
       */
      public Builder setContentBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        content_ = value;
        onChanged();
        return this;
      }

      private long timestamp_ ;
      /**
       * <code>int64 timestamp = 4;</code>
       * @return The timestamp.
       */
      @Override
      public long getTimestamp() {
        return timestamp_;
      }
      /**
       * <code>int64 timestamp = 4;</code>
       * @param value The timestamp to set.
       * @return This builder for chaining.
       */
      public Builder setTimestamp(long value) {
        
        timestamp_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int64 timestamp = 4;</code>
       * @return This builder for chaining.
       */
      public Builder clearTimestamp() {
        
        timestamp_ = 0L;
        onChanged();
        return this;
      }
      @Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:com.imv3.GroupMessage)
    }

    // @@protoc_insertion_point(class_scope:com.imv3.GroupMessage)
    private static final GroupMessage DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new GroupMessage();
    }

    public static GroupMessage getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<GroupMessage>
        PARSER = new com.google.protobuf.AbstractParser<GroupMessage>() {
      @Override
      public GroupMessage parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new GroupMessage(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<GroupMessage> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<GroupMessage> getParserForType() {
      return PARSER;
    }

    @Override
    public GroupMessage getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_imv3_GroupMessage_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_imv3_GroupMessage_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\023group_message.proto\022\010com.imv3\"L\n\014Group" +
      "Message\022\014\n\004from\030\001 \001(\003\022\n\n\002to\030\002 \001(\003\022\017\n\007con" +
      "tent\030\003 \001(\t\022\021\n\ttimestamp\030\004 \001(\003B\027\n\023com.ycj" +
      ".imv3.entityP\000b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_com_imv3_GroupMessage_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_imv3_GroupMessage_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_imv3_GroupMessage_descriptor,
        new String[] { "From", "To", "Content", "Timestamp", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
