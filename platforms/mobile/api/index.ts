import { registerPlugin } from "@capacitor/core";
import Schema, { AppIconI } from "../../web/api/index";

interface EchoPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
const Echo = registerPlugin<EchoPlugin>("Echo");

const AndroidApi: Schema = {
  async getAppIcon(name: string): Promise<AppIconI> {
    const { value } = await Echo.echo({ value: name });

    console.log("Response from native:", value);
    return { src: value, name };
  },
  async getAllAppIcons(): Promise<AppIconI[]> {
    const name = "facebook";
    const { value } = await Echo.echo({ value: name });

    console.log("Response from native:", value);
    return [{ src: value, name }];
  },
};

export default AndroidApi;
