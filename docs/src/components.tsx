
// A simple component that renders a Minecraft item given a id
// Example: <mcitem id="minecraft:diamond" />

import { title } from "process";

// Display an image for the item, the image should be in the public folder at /mods/{namespace}/{path}.png
export function Mcitem(props: { id: string }) {
    const [namespace, path] = props.id.split(':');
    const name = titleCase(path.replaceAll('_', ' '));
    return (
        <div>
            <img
                src={`/mods/${namespace}/${path}.png`}
                alt={path}
                title={path}
                style={{ width: '1em', height: '1em' }}
            />
            <p>{name}</p>
        </div>
    );
}

export function Recipe(props: { recipe: String }) {
    return (
        <p>{props.recipe}</p>
    );
}

function titleCase(str: string) {
    return str.split('_').map(word => word.charAt(0).toUpperCase() + word.slice(1)).join(' ');
}