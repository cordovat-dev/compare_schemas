package dis;

public interface IItemPermiso {
	public static final long BYTES_EN_KB = 1024;
	public static final long BYTES_EN_MB = BYTES_EN_KB*1024;
	public abstract String getCanonicalString();
}