/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package not.alexa.android.trust.api;
public interface GrantService extends android.os.IInterface
{
  /** Default implementation for GrantService. */
  public static class Default implements not.alexa.android.trust.api.GrantService
  {
    @Override public boolean grant(android.service.trust.AuthSet set) throws android.os.RemoteException
    {
      return false;
    }
    @Override public android.service.trust.AuthSet getCurrentSecurityState() throws android.os.RemoteException
    {
      return null;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements not.alexa.android.trust.api.GrantService
  {
    private static final java.lang.String DESCRIPTOR = "com.trusting.unlocktrustagent.api.GrantService";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.trusting.unlocktrustagent.api.GrantService interface,
     * generating a proxy if needed.
     */
    public static not.alexa.android.trust.api.GrantService asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof not.alexa.android.trust.api.GrantService))) {
        return ((not.alexa.android.trust.api.GrantService)iin);
      }
      return new not.alexa.android.trust.api.GrantService.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
        case TRANSACTION_grant:
        {
          data.enforceInterface(descriptor);
          android.service.trust.AuthSet _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.service.trust.AuthSet.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          boolean _result = this.grant(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_getCurrentSecurityState:
        {
          data.enforceInterface(descriptor);
          android.service.trust.AuthSet _result = this.getCurrentSecurityState();
          reply.writeNoException();
          if ((_result!=null)) {
            reply.writeInt(1);
            _result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          }
          else {
            reply.writeInt(0);
          }
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements not.alexa.android.trust.api.GrantService
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public java.lang.String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      @Override public boolean grant(android.service.trust.AuthSet set) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((set!=null)) {
            _data.writeInt(1);
            set.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_grant, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().grant(set);
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public android.service.trust.AuthSet getCurrentSecurityState() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.service.trust.AuthSet _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getCurrentSecurityState, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getCurrentSecurityState();
          }
          _reply.readException();
          if ((0!=_reply.readInt())) {
            _result = android.service.trust.AuthSet.CREATOR.createFromParcel(_reply);
          }
          else {
            _result = null;
          }
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      public static not.alexa.android.trust.api.GrantService sDefaultImpl;
    }
    static final int TRANSACTION_grant = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_getCurrentSecurityState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    public static boolean setDefaultImpl(not.alexa.android.trust.api.GrantService impl) {
      if (Stub.Proxy.sDefaultImpl == null && impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static not.alexa.android.trust.api.GrantService getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public boolean grant(android.service.trust.AuthSet set) throws android.os.RemoteException;
  public android.service.trust.AuthSet getCurrentSecurityState() throws android.os.RemoteException;
}
