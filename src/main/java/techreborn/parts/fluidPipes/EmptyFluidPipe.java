package techreborn.parts.fluidPipes;

/**
 * Created by modmuss50 on 12/05/2016.
 */
public class EmptyFluidPipe extends MultipartFluidPipe {
    @Override
    public EnumFluidPipeTypes getPipeType() {
        return EnumFluidPipeTypes.EMPTY;
    }
}
