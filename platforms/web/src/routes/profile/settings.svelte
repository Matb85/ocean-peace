<script lang="ts">
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";
  import { TextInput } from "@redinnlabs/system/Form";
  import { Button } from "@redinnlabs/system/Elements";
  import Api from "@redinn/oceanpeace-mobile/api";
  import { TimeInput, timeInputConfig } from "@redinnlabs/system/Form";
  import type { columnI } from "@redinnlabs/system/Form/TimeInput/TimeInput.svelte";
  import LanguageSelector from "$lib/LanguageSelector.svelte";

  const hours: columnI = {
    units: "h",
    data: [...Array(6).keys()],
    current: 2,
    multiplier: 1,
  };
  const minutes = timeInputConfig.minutesConfig(15 / 5);
  let h: number;
  let m: number;
  h = hours.current;
  m = minutes.current;
  Object.defineProperty(hours, "current", {
    set: val => {
      h = val;
    },
    get: () => h,
  });
  Object.defineProperty(minutes, "current", {
    set: val => {
      m = val;
    },
    get: () => m,
  });

  Api.getPreferences().then(data => {
    name = data.name;
    h = Math.floor(parseInt(data.screentime) / 60);
    m = Math.floor(parseInt(data.screentime) % 60) / 5;
  });
  let name = "";

  /** updates the preferences
   * @returns {void}
   */
  function save() {
    Api.setPreference("name", name);
    Api.setPreference("screentime", h * 60 + m + "");
  }
</script>

<FullHeading backHref="/profile">Settings</FullHeading>

<H thin>Your name</H>

<section class="card-flex-col">
  <TextInput label="Your name" placeholder="Your name" bind:value={name} />

  <H thin>Your goal screen time</H>
  <TimeInput columns={[hours, minutes]} />
  <H thin>Your language</H>

  <LanguageSelector className="text-black" />
</section>

<div class="fixed-bottom-button">
  <Button isFullWidth on:click={save}>Save changes</Button>
</div>
