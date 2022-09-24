<script lang="ts">
  import { Button, BackButton, H } from "@redinnlabs/system/Elements";
  import { TimeInput, timeInputConfig } from "@redinnlabs/system/Form";
  import type { columnI } from "@redinnlabs/system/Form/TimeInput/TimeInput.svelte";
  import Wrapper from "$lib/Wrapper.svelte";

  import Link from "$lib/Link.svelte";
  import Api from "@redinn/oceanpeace-mobile/api";
  import { goTo } from "$lib/utils";
  import { t } from "$lib/i18n";
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

  /** sets the username
   * @returns {void}
   */
  function setScreentime() {
    Api.setPreference("screentime", (h * 60 + m * 5).toString());
    Api.setPreference("setupComplete", "true");
    goTo("/");
  }
</script>

<Wrapper>
  <div slot="header" class="card-flex-col w-full">
    <Link href="/setup/1">
      <BackButton />
    </Link>
    <H tag={3}>{$t("d.setup.before")}</H>
  </div>
  <H thin className="mb-4">{$t("d.setup.screentime")}</H>
  <TimeInput columns={[hours, minutes]} />

  <Link on:click={setScreentime} className="absolute bottom-5" href="/">
    <Button>{$t("d.setup.begin")}</Button>
  </Link>
</Wrapper>
