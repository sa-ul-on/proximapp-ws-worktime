package com.proximapp.worktime.entity;

import java.util.Date;

public class Worktime {

	private long id;
	private Date dateFrom, dateTo;
	private long companyId, userId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateto) {
		dateTo = dateto;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	@Override
	public String toString() {
		return "Worktime{" +
				"dateFrom=" + dateFrom +
				", DateTo=" + dateTo +
				", companyId=" + companyId +
				", userID=" + userId +
				'}';
	}

}