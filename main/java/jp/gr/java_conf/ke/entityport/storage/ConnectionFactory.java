package jp.gr.java_conf.ke.entityport.storage;

public interface ConnectionFactory {

    StorageConnection createDBConnection(String schemaName);

    StorageConnection createFileConnection(String schemaName);

    StorageConnection createMemoryConnection(String schemaName);
}
