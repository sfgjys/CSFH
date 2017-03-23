package com.minji.cufcs.bean;

import java.util.List;

public class PollingSingleSave {

	public String id;
	public List<SingleContent> pds;

	public static class SingleContent {
		public String patrolid;
		public String enumid;
		public int istype;
		public String contents;
		public String getPatrolid() {
			return patrolid;
		}
		public void setPatrolid(String patrolid) {
			this.patrolid = patrolid;
		}
		public String getEnumid() {
			return enumid;
		}
		public void setEnumid(String enumid) {
			this.enumid = enumid;
		}
		public int getIstype() {
			return istype;
		}
		public void setIstype(int istype) {
			this.istype = istype;
		}
		public String getContents() {
			return contents;
		}
		public void setContents(String contents) {
			this.contents = contents;
		}
		public SingleContent(String patrolid, String enumid, int istype,
				String contents) {
			super();
			this.patrolid = patrolid;
			this.enumid = enumid;
			this.istype = istype;
			this.contents = contents;
		}
		public SingleContent() {
			super();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<SingleContent> getPds() {
		return pds;
	}

	public void setPds(List<SingleContent> pds) {
		this.pds = pds;
	}

	public PollingSingleSave() {
		super();
	}

	public PollingSingleSave(String id, List<SingleContent> pds) {
		super();
		this.id = id;
		this.pds = pds;
	}

	
}
