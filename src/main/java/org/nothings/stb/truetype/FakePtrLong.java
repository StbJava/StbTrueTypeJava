package org.nothings.stb.truetype;

import java.util.Arrays;

public class FakePtrLong {
	private long[] array;
	public int offset;

	public FakePtrLong(FakePtrLong ptr, int offset) {
		array = ptr.array;
		this.offset = ptr.offset + offset;
	}

	public FakePtrLong(long[] data, int offset) {
		array = data;
		this.offset = offset;
	}

	public FakePtrLong(long[] data) {
		this(data, 0);
	}

	public FakePtrLong(long data) {
		this(new long[]{data}, 0);
	}

	public void clear(int count) {
		Arrays.fill(array, offset, offset + count, 0);
	}

	public long get() {
		return array[offset];
	}

	public void set(long value) {
		array[offset] = value;
	}

	public long getAt(int offset) {
		return array[this.offset + offset];
	}

	public void setAt(int offset, long value) {
		array[this.offset + offset] = value;
	}

	public void move(int offset) {
		this.offset += offset;
	}

	public void increase() {
		move(1);
	}

	public long getAndIncrease() {
		long result = array[offset];
		++offset;
		return result;
	}

	public void setAndIncrease(long value) {
		array[offset] = value;
		++offset;
	}

	public FakePtrLong cloneAdd(int offset) {
		return new FakePtrLong(array, this.offset + offset);
	}

	public FakePtrLong clone() {
		return new FakePtrLong(array, offset);
	}

	public void fill(long value, int count) {
		Arrays.fill(array, offset, offset + count, value);
	}

	public void memcpy(FakePtrLong b, int count) {
		System.arraycopy(b.array, b.offset, array, offset, count);
	}
}
