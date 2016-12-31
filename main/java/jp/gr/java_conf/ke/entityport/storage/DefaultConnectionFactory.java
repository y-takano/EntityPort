package jp.gr.java_conf.ke.entityport.storage;


import android.content.Context;

public class DefaultConnectionFactory implements ConnectionFactory {

    private final Context ctxt;

    protected DefaultConnectionFactory() {
        this.ctxt = null;
    }

    public DefaultConnectionFactory(Context context) {
        this.ctxt = context;
    }

    @Override
    public StorageConnection createDBConnection(String schemaName) {
        return new DatabaseConnection(ctxt, schemaName);
    }

    @Override
    public StorageConnection createFileConnection(String schemaName) {
        return new FileConnection(ctxt, schemaName);
    }

    @Override
    public StorageConnection createMemoryConnection(String schemaName) {
        return new MemoryConnection(schemaName);
    }
}
