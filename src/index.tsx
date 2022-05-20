import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-custom-notification' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const CustomNotification = NativeModules.CustomNotification
  ? NativeModules.CustomNotification
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function multiply(a: number, b: number): Promise<number> {
  return CustomNotification.multiply(a, b);
}

export function test(a: number,b: number):number{
  return CustomNotification.test(a,b);
}

export function CreateInformativeNotification(Title: String, Description: String, ImageUrl:String){
  return CustomNotification.CreateInformativeNotification(Title,Description,ImageUrl);
}