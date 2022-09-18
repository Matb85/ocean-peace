<script lang="ts">
  import Delete from "$lib/Delete.svelte";
  import Api from "@redinn/oceanpeace-mobile/api";
  import { goTo } from "$lib/utils";
  import SM from "$lib/sessionManager";

  const data = SM.goal.getProps("id", "name");

  /** delete a given goal
   * @returns {void}
   */
  async function deleteGoal() {
    await Api.deleteGoal(data.id);
    goTo("/");
  }
</script>

<Delete cancelHref="/goal?id={data.id}" type="goal" name={data.name} callback={deleteGoal} />
