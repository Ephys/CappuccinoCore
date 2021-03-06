package paoo.cappuccino.util.hasher;

/**
 * Base hash holder class containing methods shared by every other hash holder.
 *
 * @author Guylian Cox
 */
public abstract class BaseHashHolder implements IHashHolder {

  private String algorithmVersion;
  private byte[] salt;
  private byte[] hash;

  public BaseHashHolder() {
  }

  /**
   * Clones an already existing hash data holder.
   *
   * @param hashData the holder to clone.
   */
  public BaseHashHolder(IHashHolderDto hashData) {
    salt = hashData.getSalt();
    hash = hashData.getHash();
  }

  @Override
  public byte[] getHash() {
    return hash.clone();
  }

  @Override
  public void setHash(byte[] hash) {
    this.hash = hash.clone();
  }

  @Override
  public byte[] getSalt() {
    return salt.clone();
  }

  @Override
  public void setSalt(byte[] salt) {
    this.salt = salt.clone();
  }

  @Override
  public String getAlgorithmVersion() {
    return algorithmVersion;
  }

  @Override
  public void setAlgorithmVersion(String version) {
    this.algorithmVersion = version;
  }
}
