package org.nothings.stb.truetype;

class FakePtr<T> {
	private T[] array;
	public int offset;

	public FakePtr(FakePtr<T> ptr, int offset) {
		array = ptr.array;
		this.offset = ptr.offset + offset;
	}

	public FakePtr(T[] data, int offset) {
		array = data;
		this.offset = offset;
	}

	public FakePtr(T[] data) {
		this(data, 0);
	}

	public T get() {
		return array[offset];
	}

	public void set(T value) {
		array[offset] = value;
	}

	public T getAt(int offset) {
		return array[this.offset + offset];
	}

	public void setAt(int offset, T value) {
		array[this.offset + offset] = value;
	}

	public void move(int offset) {
		this.offset += offset;
	}

	public void increase() {
		move(1);
	}

	public T getAndIncrease() {
		T result = array[offset];
		++offset;
		return result;
	}

	public void setAndIncrease(T value) {
		array[offset] = value;
		++offset;
	}

	public FakePtr<T> cloneAdd(int offset) {
		return new FakePtr<T>(array, this.offset + offset);
	}

	public FakePtr<T> clone() {
		return new FakePtr<T>(array, offset);
	}
}
