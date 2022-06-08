<script lang="ts">
  import "@redinnlabs/system/utils/base.css";
  import { Button, Heading, Background } from "@redinnlabs/system/Elements";
  const minutes: number = 148;
</script>

<Heading tag={3} className="text-shadow mt-7 text-white">Summary</Heading>

<div class="text-shadow w-10/12 text-center text-white">
  <Heading tag={3}>
    {minutes < 59 ? "" : Math.floor(minutes / 60) + "h"}
    {minutes % 60 == 0 ? "" : (minutes % 60) + "min"}
  </Heading>
  <Heading tag={6} className="!font-normal">of focus</Heading>
</div>

<a href="/">
  <Heading tag={5} className="text-center">Notifications received</Heading>
  <Button>continue</Button>
</a>

<Background className="absolute -z-0" />
