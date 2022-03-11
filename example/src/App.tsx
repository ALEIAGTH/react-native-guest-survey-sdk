import React, { useEffect } from 'react';
import { StyleSheet, View, Button } from 'react-native';
import GuestSurveySdk from 'react-native-guest-survey-sdk';
var testValues = ['<ConfirmITProgramKey>', 'onFeedbackButton', '<Geo4CastSecretSDKKey>', '<UserID>']

export default function App() {
  useEffect(() => {
    GuestSurveySdk.configure(
      testValues[2],
      testValues[0],
      'en',
      true,
      (done: any) => {
        console.log(done);
      }
    );
  }, []);

  return (
    <View style={styles.container}>
      <Button
        title="1.Configure"
        onPress={async () => {
          GuestSurveySdk.configure(
            testValues[2],
            testValues[0],
            'en',
            true,
            (done: any) => {
              console.log('configure is done:', done);
            }
          );
        }}
        accessibilityLabel="Configure"
        color="blue"
      />

      <Button
        title="2.Start Collection"
        onPress={async () => {
          GuestSurveySdk.startCollection(testValues[3]);
        }}
        accessibilityLabel="startCollection"
        color="black"
      />
      {
        <Button
          title="3.Stop Collection"
          onPress={async () => {
            GuestSurveySdk.stopCollection();
          }}
          accessibilityLabel="startCollection"
          color="red"
        />
      }
      <Button
        title="4.Get User SurveyList as json"
        onPress={async () => {
          GuestSurveySdk.getUserSurveys((values: [any]) => {
            console.log(values);
          });
        }}
        accessibilityLabel="User SurveyList as json"
        color="gray"
      />
      <Button
        title="5.Trigger Confirmit Sample Survey Event"
        onPress={async () => {
          GuestSurveySdk.triggerSurvey(testValues[0], testValues[1], { 'language': 'en' });
        }}
        accessibilityLabel="Trigger Sample Survey Event"
        color="green"
      />

      <Button
        title="6.Add notification token"
        onPress={async () => {
          GuestSurveySdk.addPushNotificationToken(testValues[4]);

        }}
        accessibilityLabel="Add Notification Token"
        color="yellow"
      />

      <Button
        title="7.Send Geo4Cast log"
        onPress={async () => {
          GuestSurveySdk.sendLog();
        }}
        accessibilityLabel="Send Collection Logs"
        color="gray"
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
