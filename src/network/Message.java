// 
// Decompiled by Procyon v0.6.0
// 

package network;

import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import network.inetwork.IMessage;

public class Message implements IMessage
{
    public byte command;
    private ByteArrayOutputStream os;
    private DataOutputStream dos;
    private ByteArrayInputStream is;
    private DataInputStream dis;
    
    public Message(final int n) {
        this((byte)n);
    }
    
    public Message(final byte command) {
        this.command = command;
        this.os = new ByteArrayOutputStream();
        this.dos = new DataOutputStream(this.os);
    }
    
    public Message(final byte command, final byte[] buf) {
        this.command = command;
        this.is = new ByteArrayInputStream(buf);
        this.dis = new DataInputStream(this.is);
    }
    
    @Override
    public DataOutputStream writer() {
        return this.dos;
    }
    
    @Override
    public DataInputStream reader() {
        return this.dis;
    }
    
    @Override
    public byte[] getData() {
        return this.os.toByteArray();
    }
    
    @Override
    public void cleanup() {
        try {
            if (this.is != null) {
                this.is.close();
            }
            if (this.os != null) {
                this.os.close();
            }
            if (this.dis != null) {
                this.dis.close();
            }
            if (this.dos != null) {
                this.dos.close();
            }
        }
        catch (final IOException ex) {}
    }
    
    @Override
    public void dispose() {
        this.cleanup();
        this.dis = null;
        this.is = null;
        this.dos = null;
        this.os = null;
    }
    
    @Override
    public int read() throws IOException {
        return this.reader().read();
    }
    
    @Override
    public int read(final byte[] b) throws IOException {
        return this.reader().read(b);
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        return this.reader().read(b, off, len);
    }
    
    @Override
    public boolean readBoolean() throws IOException {
        return this.reader().readBoolean();
    }
    
    @Override
    public byte readByte() throws IOException {
        return this.reader().readByte();
    }
    
    @Override
    public short readShort() throws IOException {
        return this.reader().readShort();
    }
    
    @Override
    public int readInt() throws IOException {
        return this.reader().readInt();
    }
    
    @Override
    public long readLong() throws IOException {
        return this.reader().readLong();
    }
    
    @Override
    public float readFloat() throws IOException {
        return this.reader().readFloat();
    }
    
    @Override
    public double readDouble() throws IOException {
        return this.reader().readDouble();
    }
    
    @Override
    public char readChar() throws IOException {
        return this.reader().readChar();
    }
    
    @Override
    public String readUTF() throws IOException {
        return this.reader().readUTF();
    }
    
    @Override
    public void readFully(final byte[] b) throws IOException {
        this.reader().readFully(b);
    }
    
    @Override
    public void readFully(final byte[] b, final int off, final int len) throws IOException {
        this.reader().readFully(b, off, len);
    }
    
    @Override
    public int readUnsignedByte() throws IOException {
        return this.reader().readUnsignedByte();
    }
    
    @Override
    public int readUnsignedShort() throws IOException {
        return this.reader().readUnsignedShort();
    }
    
    @Override
    public void write(final byte[] b) throws IOException {
        this.writer().write(b);
    }
    
    @Override
    public void write(final int b) throws IOException {
        this.writer().write(b);
    }
    
    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        this.writer().write(b, off, len);
    }
    
    @Override
    public void writeBoolean(final boolean v) throws IOException {
        this.writer().writeBoolean(v);
    }
    
    @Override
    public void writeByte(final int v) throws IOException {
        this.writer().writeByte(v);
    }
    
    @Override
    public void writeBytes(final String s) throws IOException {
        this.writer().writeBytes(s);
    }
    
    @Override
    public void writeChar(final int v) throws IOException {
        this.writer().writeChar(v);
    }
    
    @Override
    public void writeChars(final String s) throws IOException {
        this.writer().writeChars(s);
    }
    
    @Override
    public void writeDouble(final double v) throws IOException {
        this.writer().writeDouble(v);
    }
    
    @Override
    public void writeFloat(final float v) throws IOException {
        this.writer().writeFloat(v);
    }
    
    @Override
    public void writeInt(final int v) throws IOException {
        this.writer().writeInt(v);
    }
    
    @Override
    public void writeLong(final long v) throws IOException {
        this.writer().writeLong(v);
    }
    
    @Override
    public void writeShort(final int v) throws IOException {
        this.writer().writeShort(v);
    }
    
    @Override
    public void writeUTF(final String str) throws IOException {
        this.writer().writeUTF(str);
    }
    
    @Override
    public BufferedImage readImage() throws IOException {
        final byte[] buf = new byte[this.readInt()];
        this.read(buf);
        return ImageIO.read(new ByteArrayInputStream(buf));
    }
    
    @Override
    public void writeImage(final BufferedImage im, final String formatName) throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(im, formatName, output);
        final byte[] byteArray = output.toByteArray();
        this.writeInt(byteArray.length);
        this.write(byteArray);
    }
}
