declare type GuestSurveySdkType = {
    configure(secretKey: string, language: string, enableDebugging: boolean, callback: (callback: any) => void): void;
    startCollection(userId: string): void;
    stopCollection(): void;
    addPushNotificationToken(token: string): void;
    triggerSurvey(program_id: string, withEvent: string): void;
    getUserSurveys(callback: (callback: any) => void): void;
    sendLog(): void;
};
declare const _default: GuestSurveySdkType;
export default _default;
