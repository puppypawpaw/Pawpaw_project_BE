package kr.co.pawpaw.domainrdb.sms.domain;

public enum SmsUsagePurpose {
    SIGN_UP(3);

    final int limitPerDay;

    SmsUsagePurpose(final int limitPerDay) {
        this.limitPerDay = limitPerDay;
    }

    public int getLimitPerDay() {
        return limitPerDay;
    }
}
