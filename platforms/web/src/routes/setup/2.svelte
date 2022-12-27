<script lang="ts">
  import { Button, BackButton, H } from "@redinnlabs/system/Elements";
  import { TimeInput, timeInputConfig } from "@redinnlabs/system/Form";
  import type { columnI } from "@redinnlabs/system/Form/TimeInput/TimeInput.svelte";
  import Wrapper from "$lib/Wrapper.svelte";

  import Link from "$lib/Link.svelte";
  import Api from "@redinn/oceanpeace-mobile/api";
  import { goTo } from "$lib/utils";
  import { t } from "$lib/i18n";

  import SM from "$lib/sessionManager";
  SM.action.setProp("nativeBackUrl", "/setup/1");

  const hours: columnI = {
    id: "hours",
    units: "h",
    data: [...Array(6).keys()],
    current: 2,
    multiplier: 1,
  };
  const minutes = timeInputConfig.minutesConfig("minutes", 15 / 5);

  /** sets the username
   * @returns void
   */
  function setScreentime() {
    Api.setPreference("screentime", (hours.current * 60 + minutes.current * 5).toString());
    Api.setPreference("setupComplete", "true");
    goTo("/");
  }
</script>

<Wrapper>
  <div slot="header" class="card-flex-col w-full text-center">
    <Link href="/setup/1">
      <BackButton />
    </Link>
    <H tag={3}>{$t("d.setup.before")}</H>
  </div>
  <H thin className="mb-4 text-center">{$t("d.setup.screentime")}</H>
  <TimeInput columns={[hours, minutes]} />

  <Link on:click={setScreentime} className="absolute bottom-5" href="/">
    <Button>{$t("d.setup.begin")}</Button>
  </Link>
</Wrapper>
