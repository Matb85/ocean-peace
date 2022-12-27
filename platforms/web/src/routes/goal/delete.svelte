<script lang="ts">
  import Delete from "$lib/Delete.svelte";
  import { goTo } from "$lib/utils";
  import Api from "@redinn/oceanpeace-mobile/api";
  import SM from "$lib/sessionManager";
  SM.action.setProp("nativeBackUrl", "/goal/delete");

  const data = SM.goal.getProps("id", "name");

  /** delete a given goal
   * @returns void
   */
  async function deleteGoal() {
    await Api.deleteGoal(data.id);
    goTo("/");
  }
</script>

<Delete cancelHref="/goal?id={data.id}" type="goal" name={data.name} callback={deleteGoal} />
