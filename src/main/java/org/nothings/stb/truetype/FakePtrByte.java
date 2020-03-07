package org.nothings.stb.truetype;

import java.util.Arrays;

class FakePtrByte {
	private short[] array;
	public int offset;

	public FakePtrByte(FakePtrByte ptr, int offset) {
		array = ptr.array;
		this.offset = ptr.offset + offset;
	}

	public FakePtrByte(short[] data, int offset) {
		array = data;
		this.offset = offset;
	}

	public FakePtrByte(short[] data) {
		this(data, 0);
	}

	public void memset(int value, int count) {
		Arrays.fill(array, offset, offset + count, (byte)value);
	}

	public void clear(int count) {
		Arrays.fill(array, offset, offset + count, (byte)0);
	}

	public int get() {
		return array[offset] & 0xff;
	}

	public void set(int value) {
		array[offset] = (short)value;
	}

	public int getAt(int offset) {
		return array[this.offset + offset] & 0xff;
	}

	public void setAt(int offset, int value) {
		array[this.offset + offset] = (short)value;
	}

	public void move(int offset) {
		this.offset += offset;
	}

	public void increase() {
		move(1);
	}

	public int getAndIncrease() {
		int result = array[offset] & 0xff;
		++offset;
		return result;
	}

	public void setAndIncrease(int value) {
		array[offset] = (short)value;
		++offset;
	}

	public FakePtrByte cloneAdd(int offset) {
		return new FakePtrByte(array, this.offset + offset);
	}

	public FakePtrByte clone() {
		return new FakePtrByte(array, offset);
	}

	public void fill(int value, int count) {
		Arrays.fill(array, offset, offset + count, (short)value);
	}

	public void fillAndIncrease(int value, int count) {
		fill(value, count);
		offset += count;
	}

	public void memcpy(FakePtrByte b, int count) {
		System.arraycopy(b.array, b.offset, array, offset, count);
	}
}
