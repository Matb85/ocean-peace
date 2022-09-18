<script lang="ts">
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";
  import { mdiCog } from "@mdi/js";
  import { IconButton, Button } from "@redinnlabs/system/Elements";
  import { BarChart, CircleChart, FlatChart, ChartKey } from "@redinnlabs/system/Charts";
  import Link from "$lib/Link.svelte";
</script>

<FullHeading backHref="/">Profile</FullHeading>

<Link href="/profile/settings" className="absolute top-5 right-3">
  <IconButton secondary d={mdiCog} />
</Link>

<img class="shadow-wrapper w-1/2 aspect-square object-cover rounded-2xl mt-8" src="/profile.jpg" alt="profile" />
<H noMargins thin>John Doe</H>

<section class="shadow-wrapper card-flex-col py-4 gap-2">
  <H noMargins thin>Your balance</H>
  <h2 class="score">425 points</h2>
  <Button>see the leaderboard</Button>
</section>

<H thin>Performance</H>
<p class="-mt-3">last 7 days</p>

<BarChart
  maxTime={200}
  data={[
    [{ color: "#BAEEBD", value: 60 }],
    [{ color: "#BAEEBD", value: 30 }],
    [{ color: "#BAEEBD", value: 120 }],
    [{ color: "#BAEEBD", value: 50 }],
    [{ color: "#BAEEBD", value: 67 }],
    [{ color: "#BAEEBD", value: 90 }],
    [{ color: "#BAEEBD", value: 10 }],
  ]}
/>

<H thin>Average Success</H>
<section class="w-11/12 grid grid-cols-2 gap-2">
  <CircleChart className="w-full" color="#BAEEBD">
    <H tag={3} noMargins className="wh-full flex items-center justify-center leading-none opacity-60">77%</H>
  </CircleChart>
  <div class="shadow-wrapper block w-full h-min p-4 text-center self-center">
    <p>Most difficult Goal</p>
    <b>Check the news twice a day</b>
  </div>
</section>

<H noMargins thin>Screen Time</H>
<p class="-mt-3">last 7 days</p>

<BarChart
  maxTime={200}
  data={[
    [
      { color: "#3772FF", value: 60 },
      { color: "#FCBA04", value: 20 },
    ],
    [
      { color: "#3772FF", value: 30 },
      { color: "#FCBA04", value: 60 },
    ],
    [
      { color: "#3772FF", value: 120 },
      { color: "#FCBA04", value: 40 },
    ],
    [
      { color: "#3772FF", value: 50 },
      { color: "#FCBA04", value: 70 },
    ],
    [
      { color: "#3772FF", value: 67 },
      { color: "#FCBA04", value: 30 },
    ],
    [
      { color: "#3772FF", value: 90 },
      { color: "#FCBA04", value: 90 },
    ],
    [
      { color: "#3772FF", value: 10 },
      { color: "#FCBA04", value: 50 },
    ],
  ]}
/>

<div class="w-10/12">
  <FlatChart
    columns={[
      { color: "#3772FF", value: 60 },
      { color: "#FCBA04", value: 40 },
    ]}
  />
</div>
<div class="w-10/12">
  <ChartKey
    isVertical
    data={[
      { color: "#F8F5FA", text: "Your Screen Time limit" },
      { color: "#3772FF", text: "Samsung" },
      { color: "#FCBA04", text: "Macbook" },
    ]}
  />
</div>

<style lang="postcss">
  .score {
    @apply text-4xl;
    background: -webkit-linear-gradient(theme("colors.primary1.DEFAULT"), theme("colors.primary2.DEFAULT"));
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
  }
</style>
