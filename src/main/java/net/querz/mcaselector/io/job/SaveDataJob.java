package net.querz.mcaselector.io.job;

import net.querz.mcaselector.io.Job;
import net.querz.mcaselector.io.RegionDirectories;

public abstract class SaveDataJob<T> extends Job {

	private final T data;

	public SaveDataJob(RegionDirectories dirs, T data) {
		super(dirs, PRIORITY_LOW);
		this.data = data;
	}

	public T getData() {
		return data;
	}

	@Override
	public void run() {
		execute();
	}

	public abstract void execute();

	// can be overwritten to indicate that this job can be skipped in favor for load and process jobs
	public boolean canSkip() {
		return false;
	}
}