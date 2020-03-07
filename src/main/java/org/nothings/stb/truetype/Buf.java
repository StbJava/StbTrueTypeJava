package org.nothings.stb.truetype;

public class Buf {
	public int cursor;
	public FakePtrByte data;
	public int size;

	public Buf(FakePtrByte p, int size) {
		data = p;
		this.size = (int) size;
		cursor = 0;
	}

	public int stbtt__buf_get8() {
		if (cursor >= size)
			return 0;
		return data.getAt(cursor++);
	}

	public int stbtt__buf_peek8() {
		if (cursor >= size)
			return 0;
		return data.getAt(cursor);
	}

	public void stbtt__buf_seek(int o) {
		cursor = o > size || o < 0 ? size : o;
	}

	public void stbtt__buf_skip(int o) {
		stbtt__buf_seek(cursor + o);
	}

	public long stbtt__buf_get(int n) {
		long v = (long) 0;
		int i = 0;
		for (i = 0; i < n; i++)
			v = (v << 8) | stbtt__buf_get8();
		return v;
	}

	public Buf stbtt__buf_range(int o, int s) {
		if (o < 0 || s < 0 || o > size || s > size - o)
			return null;

		return new Buf(data.cloneAdd(o), s);
	}

	public Buf stbtt__cff_get_index() {
		int count = 0;
		int start = 0;
		int offsize = 0;
		start = cursor;
		count = (int) stbtt__buf_get(2);
		if (count != 0) {
			offsize = stbtt__buf_get8();
			stbtt__buf_skip(offsize * count);
			stbtt__buf_skip((int) (stbtt__buf_get(offsize) - 1));
		}

		return stbtt__buf_range(start, cursor - start);
	}

	public long stbtt__cff_int() {
		int b0 = (int) stbtt__buf_get8();
		if (b0 >= 32 && b0 <= 246)
			return (long) (b0 - 139);
		if (b0 >= 247 && b0 <= 250)
			return (long) ((b0 - 247) * 256 + stbtt__buf_get8() + 108);
		if (b0 >= 251 && b0 <= 254)
			return (long) (-(b0 - 251) * 256 - stbtt__buf_get8() - 108);
		if (b0 == 28)
			return stbtt__buf_get(2);
		if (b0 == 29)
			return stbtt__buf_get(4);
		return 0;
	}

	public void stbtt__cff_skip_operand() {
		int v = 0;
		int b0 = (int) stbtt__buf_peek8();
		if (b0 == 30) {
			stbtt__buf_skip(1);
			while (cursor < size) {
				v = stbtt__buf_get8();
				if ((v & 0xF) == 0xF || v >> 4 == 0xF)
					break;
			}
		} else {
			stbtt__cff_int();
		}
	}

	public Buf stbtt__dict_get(int key) {
		stbtt__buf_seek(0);
		while (cursor < size) {
			int start = cursor;
			int end = 0;
			int op = 0;
			while (stbtt__buf_peek8() >= 28)
				stbtt__cff_skip_operand();
			end = cursor;
			op = stbtt__buf_get8();
			if (op == 12)
				op = stbtt__buf_get8() | 0x100;
			if (op == key)
				return stbtt__buf_range(start, end - start);
		}

		return stbtt__buf_range(0, 0);
	}

	public void stbtt__dict_get_ints(int key, int outcount, FakePtrLong _out_) {
		int i = 0;
		Buf operands = stbtt__dict_get(key);
		for (i = 0; i < outcount && operands.cursor < operands.size; i++)
			_out_.setAt(i, operands.stbtt__cff_int());
	}

	public long stbtt__dict_get_ints(int key) {
		FakePtrLong temp = new FakePtrLong(new long[1]);

		stbtt__dict_get_ints(key, 1, temp);

		return temp.getAt(0);
	}

	public int stbtt__cff_index_count() {
		stbtt__buf_seek(0);
		return (int) stbtt__buf_get(2);
	}

	public Buf stbtt__cff_index_get(int i) {
		int count = 0;
		int offsize = 0;
		int start = 0;
		int end = 0;
		stbtt__buf_seek(0);
		count = (int) stbtt__buf_get(2);
		offsize = stbtt__buf_get8();
		stbtt__buf_skip(i * offsize);
		start = (int) stbtt__buf_get(offsize);
		end = (int) stbtt__buf_get(offsize);
		return stbtt__buf_range(2 + (count + 1) * offsize + start, end - start);
	}

	public static Buf stbtt__get_subrs(Buf cff, Buf fontdict) {
		long subrsoff = (long) 0;

		long[] private_loc = new long[2];
		private_loc[0] = 0;
		private_loc[1] = 0;

		fontdict.stbtt__dict_get_ints(18, 2, new FakePtrLong(private_loc));
		if (private_loc[1] == 0 || private_loc[0] == 0)
			return null;
		Buf pdict = cff.stbtt__buf_range((int) private_loc[1], (int) private_loc[0]);
		pdict.stbtt__dict_get_ints(19, 1, new FakePtrLong(subrsoff));
		if (subrsoff == 0)
			return null;
		cff.stbtt__buf_seek((int) (private_loc[1] + subrsoff));
		return cff.stbtt__cff_get_index();
	}

	public Buf stbtt__get_subr(int n) {
		int count = stbtt__cff_index_count();
		int bias = 107;
		if (count >= 33900)
			bias = 32768;
		else if (count >= 1240)
			bias = 1131;
		n += bias;
		if (n < 0 || n >= count)
			return null;
		return stbtt__cff_index_get(n);
	}
}
