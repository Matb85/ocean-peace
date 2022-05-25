<script lang="ts">
  import "@redinnlabs/system/utils/base.css";
  import { BackButton, Button, Heading } from "@redinnlabs/system/Elements";
  import { Schedule, Preset } from "@redinnlabs/system/Units";
  import W from "@redinnlabs/system/assets/icon-working.svg";
  import A from "@redinnlabs/system/assets/icon-add.svg";

  function presetPressed() {
    location.href='/presetEdit';
  }
</script>

<!-- C O N T E N T -->

<div class="content">
  <!-- svelte-ignore component-name-lowercase -->

  <div class="title">
    <Heading tag={3}>Focus</Heading>
  </div>

  <div class="preset_title">
    <Heading tag={5} className="!font-normal">Presets</Heading>
  </div>
  <div class="presets">
    {#each Array(3) as _, i}
      <Preset src={W} label="Bottom text {i}" onClick={presetPressed}/>
    {/each}
    <Preset src={A} noShadowWrapper="true" onClick={presetPressed}/>
  </div>

  <div class="schedule_title">
    <Heading tag={5} className="!font-normal">Schedule</Heading>
  </div>
  <div class="schedule">
    {#each Array(3) as _, i}
      <Schedule src={W} title="Event example" />
    {/each}
    <Button secondary="true">Add a Rule</Button>
  </div>

</div>

<div class="back">
  <a href="/index">
    <BackButton/>
  </a>
</div>

<style lang="postcss">
  .back {
    @apply fixed
            top-5 left-3
            min-h-fit min-w-fit;
  }
  .title {
    @apply absolute
            top-7;
  }
  .content {
    @apply flex flex-col 
            items-center place-content-center
            select-none
            mb-16 mt-28
            gap-14;
    pointer-events: all;
  }
  .content .preset_title {
    @apply -mb-10;
  }
  .content .presets {
    @apply grid
            grid-cols-2
            gap-4;
  }

  .content .schedule_title {
    @apply -mb-10;
  }
  .content .schedule {
    @apply w-11/12 mx-auto
        grid grid-cols-1 gap-4
            place-items-center;
  }
</style>
