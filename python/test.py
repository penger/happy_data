import re;


'''补全日期'''
def full_date(date):
    if not date:
        date = '01'
    if int(date) < 10 and len(date) < 2:
        date = '0' + date
    return date

'''检查年份并统一时间'''
def standard_year(sent):
    sent = sent.replace(' ', '')
    pattern_year = re.compile('[0-9]{4}年')
    pattern_month = re.compile('[0-9]{1,4}月')
    pattern_day = re.compile('[0-9]{1,4}日')
    default_day = ''
    default_month = ''
    month = pattern_month.findall(sent)
    day = pattern_day.findall(sent)
    year = pattern_year.findall(sent)
    if year:
        year = year[0].replace('年', '')
        if month:
            default_month = month[0].replace('月', '')
        if day:
            default_day = day[0].replace('日', '')
        if year:
            date_new = year + full_date(default_month) + full_date(default_day)
        else:
            date_new = ''
    else:
        return ''
    return date_new


print(standard_year('2023年6774月待'))