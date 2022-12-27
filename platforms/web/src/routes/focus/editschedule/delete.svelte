<script lang="ts">
  import Delete from "$lib/Delete.svelte";
  import Api from "@redinn/oceanpeace-mobile/api";
  import { goTo } from "$lib/utils";
  import SM from "$lib/sessionManager";
  SM.action.setProp("nativeBackUrl", "/focus/editschedule/delete");

  const data = SM.schedule.getProps("id", "name");
  /** delete a given schedule
   * @returns void
   */
  async function deleteSchedule() {
    await Api.deleteSchedule(data.id);
    goTo("/focus");
  }
</script>

<Delete cancelHref="/focus/schedule?id={data.id}" type="schedule" name={data.name} callback={deleteSchedule} />
