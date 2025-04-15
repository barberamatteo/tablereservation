class DateUtils{

    private static getTodayDMY(): string[]{
        const date = new Date();
        const month: string = (
            (date.getMonth() + 1).toString().length === 1
            ? '0' + (date.getMonth() + 1).toString()
            : (date.getMonth() + 1).toString()
        );
        const day: string = (
            date.getDate().toString().length === 1
            ? '0' + date.getDate().toString()
            : date.getDate().toString()
        );
        return [day, month, date.getFullYear().toString()];
    }

    static toLocaleDate(ymd: string){
        const [y, m, d] = ymd.split('-');
        return `${d}-${m}-${y}`;
    }
    static today(): string{
        const todayDMY = DateUtils.getTodayDMY();

        return `${todayDMY[2]}-${todayDMY[1]}-${todayDMY[0]}`;
    }

    static todayLocale(): string{
        const todayDMY = DateUtils.getTodayDMY();

        return `${todayDMY[0]}-${todayDMY[1]}-${todayDMY[2]}`;
    }
}
    export default DateUtils;
