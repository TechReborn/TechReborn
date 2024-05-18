
// A simple component that renders a Minecraft item given a id
// Example: <mcitem id="minecraft:diamond" />
// Display an image for the item, the image should be in the public folder at /mods/{namespace}/{path}.png
export function Mcitem(props: { id: string }) {
    const [namespace, path] = props.id.split(':');
    return (
        <img
            src={`/mods/${namespace}/${path}.png`}
            alt={path}
            title={path}
            style={{ width: '1em', height: '1em' }}
        />
    );
}