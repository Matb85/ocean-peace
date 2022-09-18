<script>
  import { Heading as H, Icon } from "@redinnlabs/system/Elements";
  import { mdiSlack, mdiHomeVariantOutline, mdiAccountOutline } from "@mdi/js";
  import { svgGradients } from "@redinnlabs/system/utils";
  import Link from "./Link.svelte";
  const links = [
    { name: "Profile", icon: mdiAccountOutline, href: "/profile" },
    { name: "Home", icon: mdiHomeVariantOutline, href: "/" },
    { name: "Focus", icon: mdiSlack, href: "/focus" },
  ];
</script>

<nav class="fixed bottom-0 w-full bg-white h-12 shadow-md border-t-2 flex justify-around items-center">
  {#each links as link}
    <Link href={link.href} className="flex items-center gap-1">
      <Icon className="w-6" d={link.icon} fill={svgGradients.primary} />
      <H tag={6}>{link.name}</H>
    </Link>
  {/each}
</nav>
