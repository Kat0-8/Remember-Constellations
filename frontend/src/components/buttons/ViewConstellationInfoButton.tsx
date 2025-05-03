import { Button } from 'antd';
import {ProfileFilled} from "@ant-design/icons";

export const ViewConstellationButton = ({
                                            onOpen,
                                        }: {
    onOpen: () => void;
}) => (
    <Button onClick={onOpen} icon = {<ProfileFilled />}>
        View Constellation Info
    </Button>
);