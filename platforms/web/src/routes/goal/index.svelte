<script lang="ts">
  import { BackButton, Icon, Heading, Aquarium, EditButton } from "@redinnlabs/system/Elements";
  import { mdiCheck } from "@mdi/js";
  import { PieChart } from "@redinnlabs/system/Charts";
  import Cutout from "$lib/Cutout.svelte";
</script>

<!-- C O N T E N T -->
<div class="w-full h-80 absolute -z-50 top-0">
  <Aquarium percent={80} />
  <Cutout className="w-full bottom-0 absolute" />
  <svg width="full" height="full">
    <rect
      width="full"
      height="full"
      style="fill: linear-gradient(180deg, rgba(255, 255, 255, 0) 22.4%, #FFFFFF 100%)"
    />
  </svg>
</div>

<div class="content">
  <div class="title">
    <Heading tag={3}>Goal</Heading>
  </div>

  <div class="goal-graph">
    <Heading tag={5} className="text-center !font-normal w-9/12">Limit Social Media to 90min a day</Heading>
    <div class="graph">
      <PieChart
        className="w-64 h-64"
        maxValue="200"
        data={[
          { color: "#3772FF", value: 110 },
          { color: "#FCBA04", value: 40 },
        ]}
      >
        <div class="w-full h-full flex flex-col items-center justify-center gap-2">
          <Heading tag={3}>24 min</Heading>
          <Heading tag={3} className="!font-normal">left</Heading>
        </div>
      </PieChart>
    </div>
  </div>

  <div class="last-graph">
    <Heading tag={5} className="text-center !font-normal">Last 7 days</Heading>
    <div class="graphs">
      {#each Array(4) as _, i}
        <PieChart
          className="w-20 h-20"
          maxValue="200"
          data={[
            { color: "#3772FF", value: 100 },
            { color: "#FCBA04", value: 30 },
          ]}
        />
      {/each}
    </div>
    <div class="graphs">
      {#each Array(3) as _, i}
        <PieChart
          className="w-20 h-20"
          maxValue="200"
          data={[
            { color: "#3772FF", value: 100 },
            { color: "#FCBA04", value: 30 },
          ]}
        >
          <div class="w-full h-full bg-[#CFEED1] text-[#03A108] flex items-center justify-center">
            <Icon d={mdiCheck} className="fill-current w-32" />
          </div>
        </PieChart>
      {/each}
    </div>
  </div>
  <div class="apps">
    <Heading tag={5} className="text-center !font-normal">Selected apps</Heading>
  </div>
</div>

<div class="back">
  <a href="/">
    <BackButton />
  </a>
</div>
<div class="edit">
  <a href="/goal/edit/1">
    <EditButton />
  </a>
</div>

<style lang="postcss">
  /* F I X E D   I T E M S */
  .back {
    @apply absolute
        top-5 left-3
        min-h-fit min-w-fit;
  }
  .edit {
    @apply absolute
        top-5 right-3
        min-h-fit min-w-fit;
  }
  .title {
    @apply absolute
            top-7;
  }
  /* C O N T E N T   E L E M E N T S */

  .content {
    @apply flex flex-col 
        items-center place-content-center
        select-none
        mb-16 mt-28
        gap-10
        z-10;
    pointer-events: all;
    scrollbar-width: none;
  }

  .content .goal-graph {
    @apply grid
        grid-cols-1
        items-center
        place-items-center
        select-none
        w-11/12;
  }

  .content .last-graph {
    @apply grid
        grid-cols-1
        items-center
        place-items-center
        select-none
        w-11/12;
  }
  .content .last-graph .graphs {
    @apply flex
        items-center
        justify-center
        select-none
        gap-0;
  }

  .content .apps {
    @apply grid
        grid-cols-1
        items-center
        place-items-center
        select-none
        w-11/12;
  }

  .content .goal-graph .graph {
    @aply flex;
  }
</style>
