<script lang="ts">
  
  import "@redinnlabs/system/utils/base.css";
  import { Button, Icon, Heading } from "@redinnlabs/system/Elements";
  import { Goal } from "@redinnlabs/system/Units";
  import { mdiPencil } from '@mdi/js';
</script>

<main>
  <!-- content -->
  <div class="content">
    <div class="startFocus_btn" >
      <Button>Start a focus session</Button>
    </div>
    <div class="goals_title">
      <Heading tag={6}>Your Goals</Heading>
    </div>
    <div class="goals">
      {#each Array(4) as _, i}
        <Goal title={"Finish this finally Bisu"} info={"Whole project left xd"}></Goal>
      {/each}
        <Button secondary=true >Add Goal</Button>
    </div>
  </div>

</main>

<style lang="postcss">
  :root {
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell, "Open Sans",
      "Helvetica Neue", sans-serif;
  }
  .content {
    display: flex;
    flex-direction: column;
    align-items: center;
    place-content: center;
    pointer-events: all;
    user-select: none;
    margin-bottom: 60px;
    margin-top: 10px;
  }

  .startFocus_btn {

  }

  .goals_title {
    margin-bottom: -25px;
  }

  .goals {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 21px;
    top: 520px;
  }
  
  
</style>
