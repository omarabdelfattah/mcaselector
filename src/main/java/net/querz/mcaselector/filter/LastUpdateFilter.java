package net.querz.mcaselector.filter;

import net.querz.mcaselector.io.mca.ChunkData;
import net.querz.mcaselector.text.TextHelper;
import net.querz.mcaselector.version.ChunkFilter;
import net.querz.mcaselector.version.VersionController;
import net.querz.nbt.tag.LongTag;

public class LastUpdateFilter extends LongFilter {

	public LastUpdateFilter() {
		this(Operator.AND, Comparator.EQUAL, 0);
	}

	private LastUpdateFilter(Operator operator, Comparator comparator, long value) {
		super(FilterType.LAST_UPDATE, operator, comparator, value);
	}

	@Override
	protected Long getNumber(ChunkData data) {
		if (data.getRegion() == null) {
			return 0L;
		}
		ChunkFilter chunkFilter = VersionController.getChunkFilter(data.getRegion().getData().getInt("DataVersion"));
		LongTag tag = chunkFilter.getLastUpdate(data.getRegion().getData());
		return tag == null ? 0L : tag.asLong();
	}

	@Override
	public void setFilterValue(String raw) {
		super.setFilterValue(raw);
		if (!isValid()) {
			try {
				// LastUpdate is in ticks, not seconds
				setFilterNumber(TextHelper.parseDuration(raw) * 20);
				setValid(true);
				setRawValue(raw);
			} catch (IllegalArgumentException ex) {
				setFilterNumber(0L);
				setValid(false);
			}
		}
	}

	@Override
	public String toString() {
		return "LastUpdate " + getComparator().getQueryString() + " \"" + getRawValue() + "\"";
	}

	@Override
	public String getFormatText() {
		return "duration";
	}

	@Override
	public LastUpdateFilter clone() {
		return new LastUpdateFilter(getOperator(), getComparator(), value);
	}
}
