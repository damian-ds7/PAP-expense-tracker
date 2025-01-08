package pw.edu.pl.pap.ui.groupScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.data.uiSetup.inputFields.InvitationData
import pw.edu.pl.pap.ui.common.*
import pw.edu.pl.pap.util.constants.horizontalPadding
import pw.edu.pl.pap.util.constants.verticalPadding

@Composable
fun InvitationFields(
    invitationsData: List<InvitationData>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        LazyColumn {
            items(invitationsData) { data ->
                createField(data)
            }
        }
    }
}

@Composable
private fun createField(data: InvitationData) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
            .height(50.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (data) {
                is InvitationData.NewInvitationData ->  createNewInvitation(data)
                is InvitationData.ReceivedInvitationData -> createReceivedInvitation(data)
                is InvitationData.SentInvitationData -> createSentInvitation(data)
            }
        }
    }
}

@Composable
private fun createNewInvitation(data: InvitationData.NewInvitationData){
    Column {
        Text(
            text = "Receiver: ${data.receiver.name} ${data.receiver.surname}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Group: ${data.group.name}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray
        )
    }

    Button(
        onClick = { data.onConfirm(data.receiver) },
        colors = ButtonDefaults.buttonColors(Color.Green),
//        modifier = Modifier.size(50.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Accept",
            tint = Color.White
        )
    }
}

@Composable
private fun createReceivedInvitation(data: InvitationData.ReceivedInvitationData){
    Column {
        Text(
            text = "Sender: ${data.sender.name} ${data.sender.surname}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Group: ${data.group.name}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray
        )
    }

    Button(
        onClick = { data.onConfirm(data.id) },
        colors = ButtonDefaults.buttonColors(Color.Green),
//        modifier = Modifier.size(50.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Accept",
            tint = Color.White
        )
    }

    Button(
        onClick = { data.onCancel(data.id) },
        colors = ButtonDefaults.buttonColors(Color.Red),
//        modifier = Modifier.size(50.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Deny",
            tint = Color.White
        )
    }
}

@Composable
private fun createSentInvitation(data: InvitationData.SentInvitationData){
    Column {
        Text(
            text = "Receiver: ${data.receiver.name} ${data.receiver.surname}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Group: ${data.group.name}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray
        )
    }

    Button(
        onClick = { data.onCancel(data.id) },
        colors = ButtonDefaults.buttonColors(Color.Red),
//        modifier = Modifier.size(50.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Cancel",
            tint = Color.White
        )
    }
}